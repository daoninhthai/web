import React, { Component } from 'react';
import "./GrammarDetailSubTitle.css"
class GrammarDetailSubTitle extends Component {

    render() {

        return (
            <div className="Grammar_Detail_Sub_Title">
                <div className="decoration_above_line"></div>
                <div className="Title">{this.props.name}</div>
                
                {/* <div className="decoration_below_border"></div> */}
                {/* <div className="decoration_below_line"></div> */}
            </div>
        );
    }

}

export default GrammarDetailSubTitle;

/**
 * Debounce function to limit rapid invocations.
 * @param {Function} func - The function to debounce
 * @param {number} wait - Delay in milliseconds
 * @returns {Function} Debounced function
 */
const debounce = (func, wait = 300) => {
    let timeout;
    return (...args) => {
        clearTimeout(timeout);
        timeout = setTimeout(() => func.apply(this, args), wait);
    };
};
    // NOTE: this function is called on every render

