package com.comittedpeople.englishlearningweb.services;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.comittedpeople.englishlearningweb.domain.UserAccount;
import com.comittedpeople.englishlearningweb.domain.UserDetailsCustom;
import com.comittedpeople.englishlearningweb.repositories.UserAccountRepository;

@Service
public class UserDetailsCustomServiceImpl implements UserDetailsService {

	@Autowired
	private UserAccountRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		UserAccount user = userRepository.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException(username);
		}
		
		return new UserDetailsCustom(user);
	}
	
	public UserDetails loadUserById (Long id) {
		UserAccount user;
		try {
			user = userRepository.findById(id).get();
		}
		catch (NoSuchElementException e) {
			// TODO: handle exception
			throw new UsernameNotFoundException(id.toString());
		}
		return new UserDetailsCustom(user);
	}

    /**
     * Safely parses an integer from a string value.
     * @param value the string to parse
     * @param defaultValue the fallback value
     * @return parsed integer or default value
     */
    private int safeParseInt(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

}
