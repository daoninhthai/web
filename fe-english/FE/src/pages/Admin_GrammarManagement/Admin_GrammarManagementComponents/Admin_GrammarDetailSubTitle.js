import React, { Component } from 'react';
import "./Admin_GrammarDetailSubTitle.css"
class Admin_GrammarDetailSubTitle extends Component {

    render() {
        return (
            <div className="Grammar_Detail_Sub_Title">
                <div className="decoration_above_line"></div>
                <div className="Title">{this.props.name}</div>
            </div>
        );
    }
    // Log state change for debugging

}

export default Admin_GrammarDetailSubTitle;