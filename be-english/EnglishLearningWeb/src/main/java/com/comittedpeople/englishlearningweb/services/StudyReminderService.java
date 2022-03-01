package com.comittedpeople.englishlearningweb.services;

import com.comittedpeople.englishlearningweb.domain.UserAccount;
import com.comittedpeople.englishlearningweb.repositories.UserAccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Service for managing and sending scheduled study reminders to users.
 * Uses the existing UserAccount reminder settings (reminderMonday through reminderSunday)
 * to determine which users should receive reminders on which days.
 */
@Service
public class StudyReminderService {

    private static final Logger logger = LoggerFactory.getLogger(StudyReminderService.class);
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Autowired
    private UserAccountRepository userAccountRepository;

    // Track sent reminders to avoid duplicates: "userId_date" -> timestamp
    private final Map<String, LocalDateTime> sentReminders = new ConcurrentHashMap<>();

    // Store pending reminders for retrieval via API
    private final Map<Long, List<ReminderNotification>> pendingNotifications = new ConcurrentHashMap<>();

    /**
     * Check and process study reminders. Runs every hour.
     * Evaluates each user's reminder settings against the current day of the week.
     */
    @Scheduled(cron = "0 0 * * * *")
    public void processScheduledReminders() {
        logger.info("Processing scheduled study reminders...");

        DayOfWeek today = LocalDate.now().getDayOfWeek();
        List<UserAccount> allUsers = userAccountRepository.findAll();

        int remindersSent = 0;
        for (UserAccount user : allUsers) {
            if (user.getEnabled() == null || !user.getEnabled()) {
                continue;
            }

            if (shouldSendReminder(user, today)) {
                String dedupKey = user.getId() + "_" + LocalDate.now().toString();
                if (!sentReminders.containsKey(dedupKey)) {
                    sendReminder(user);
                    sentReminders.put(dedupKey, LocalDateTime.now());
                    remindersSent++;
                }
            }
        }

        logger.info("Study reminders processed. Sent {} reminders to eligible users.", remindersSent);
        cleanupOldReminders();
    }

    /**
     * Get pending reminder notifications for a user.
     *
     * @param userId the user ID
     * @return list of pending reminder notifications
     */
    public List<ReminderNotification> getPendingReminders(Long userId) {
        return pendingNotifications.getOrDefault(userId, Collections.emptyList());
    }

    /**
     * Clear pending reminders for a user after they have been read.
     *
     * @param userId the user ID
     */
    public void clearPendingReminders(Long userId) {
        pendingNotifications.remove(userId);
        logger.debug("Cleared pending reminders for user {}", userId);
    }

    /**
     * Manually trigger a study reminder for a specific user.
     *
     * @param userId the user ID
     * @return true if the reminder was sent
     */
    public boolean sendManualReminder(Long userId) {
        Optional<UserAccount> userOpt = userAccountRepository.findById(userId);
        if (userOpt.isEmpty()) {
            logger.warn("User {} not found for manual reminder", userId);
            return false;
        }

        sendReminder(userOpt.get());
        logger.info("Manual study reminder sent to user {}", userId);
        return true;
    }

    /**
     * Get the reminder schedule for a user.
     *
     * @param userId the user ID
     * @return map of day names to whether reminders are enabled
     */
    public Map<String, Boolean> getReminderSchedule(Long userId) {
        Optional<UserAccount> userOpt = userAccountRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return Collections.emptyMap();
        }

        UserAccount user = userOpt.get();
        Map<String, Boolean> schedule = new LinkedHashMap<>();
        schedule.put("Monday", isEnabled(user.getReminderMonday()));
        schedule.put("Tuesday", isEnabled(user.getReminderTuesday()));
        schedule.put("Wednesday", isEnabled(user.getReminderWednesday()));
        schedule.put("Thursday", isEnabled(user.getReminderThursday()));
        schedule.put("Friday", isEnabled(user.getReminderFriday()));
        schedule.put("Saturday", isEnabled(user.getReminderSaturday()));
        schedule.put("Sunday", isEnabled(user.getReminderSunday()));

        return schedule;
    }

    /**
     * Get statistics about reminder sending.
     *
     * @return map with reminder stats
     */
    public Map<String, Object> getReminderStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalRemindersSentToday", sentReminders.values().stream()
                .filter(dt -> dt.toLocalDate().equals(LocalDate.now()))
                .count());
        stats.put("pendingNotificationsCount", pendingNotifications.values().stream()
                .mapToInt(List::size)
                .sum());
        stats.put("usersWithPendingReminders", pendingNotifications.size());
        return stats;
    }

    private boolean shouldSendReminder(UserAccount user, DayOfWeek day) {
        switch (day) {
            case MONDAY:    return isEnabled(user.getReminderMonday());
            case TUESDAY:   return isEnabled(user.getReminderTuesday());
            case WEDNESDAY: return isEnabled(user.getReminderWednesday());
            case THURSDAY:  return isEnabled(user.getReminderThursday());
            case FRIDAY:    return isEnabled(user.getReminderFriday());
            case SATURDAY:  return isEnabled(user.getReminderSaturday());
            case SUNDAY:    return isEnabled(user.getReminderSunday());
            default:        return false;
        }
    }

    private boolean isEnabled(Byte value) {
        return value != null && value > 0;
    }

    private void sendReminder(UserAccount user) {
        ReminderNotification notification = new ReminderNotification();
        notification.setUserId(user.getId());
        notification.setMessage("Time to study! Keep your learning streak going.");
        notification.setTimestamp(LocalDateTime.now().format(TIME_FMT));

        pendingNotifications
                .computeIfAbsent(user.getId(), k -> Collections.synchronizedList(new ArrayList<>()))
                .add(notification);

        logger.debug("Study reminder queued for user {} ({})", user.getId(), user.getUsername());
    }

    private void cleanupOldReminders() {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(2);
        sentReminders.entrySet().removeIf(entry -> entry.getValue().isBefore(cutoff));
    }

    /**
     * Represents a study reminder notification.
     */
    public static class ReminderNotification {
        private Long userId;
        private String message;
        private String timestamp;

        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public String getTimestamp() { return timestamp; }
        public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
    }
}
