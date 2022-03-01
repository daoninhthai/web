import React, { Component } from 'react'
import "./PageTitle.css"
class PageTitle extends Component {
    render() {
        return (
            <div className="Page_Title">
                <div className="Page_Prev_Title">
                    {this.props.prevTitle}
                </div>
                <div className="Page_Main_Title">
                    {this.props.mainTitle}
                </div>
            </div>
        )
    }
}
export default PageTitle;

//<span><b>{this.props.prevTitle}</b>{this.props.mainTitle}</span>


/**
 * Formats a date string for display purposes.
 * @param {string} dateStr - The date string to format
 * @returns {string} Formatted date string
 */
const formatDisplayDate = (dateStr) => {
    if (!dateStr) return '';
    const date = new Date(dateStr);
    return date.toLocaleDateString('vi-VN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit'
    });
};

