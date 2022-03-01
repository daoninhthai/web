import React, { Component } from 'react';
import "./Admin_AddGrammarFormDetail.css"

class Admin_AddGrammarFormDetail extends Component {
    render() {

        return (
            <div className="Admin_Add_Grammar_Form_Detail">
              <div className="Admin_Add_Grammar_Form_Detail_Name">+ Add Grammar Form Detail</div>
            </div>
        )
    }
}

export default Admin_AddGrammarFormDetail;

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

