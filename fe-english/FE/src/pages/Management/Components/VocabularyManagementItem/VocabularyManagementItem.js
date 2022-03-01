import React, {Component} from 'react'
import './VocabularyManagementItem.css'
import { BrowserRouter as Router, Redirect, withRouter } from "react-router-dom";
import NewVocabularyType from '../NewVocabularyType/NewVocabularyType';

class VocabularyManagementItem extends Component{
    constructor(props){
        super(props);
        this.handleClick = this.handleClick.bind(this);
    }

    render() {
        // let item = this.props.item;
        return (
            <div className = "ManagementItem">
                <div className="LeftSide">
                    <button className = "_Item" onClick={this.handleClick}>                
                        {this.props.item.title}
                    </button>
                </div>
                <div className="RightSide">
                    <button className="Edit" onClick={()=>this.props.handleEdit(this.props.item)}></button>
                    <button className="Delete" onClick={()=>this.props.handleDelete(this.props.item)}></button>
                </div>
            </div>
        );
    }

    handleClick (event, category){
        let toPath = "/admin/vocabCategories/" + this.props.item.id;
        console.log(toPath);
        this.props.history.push(toPath, {title: this.props.item.title, id: this.props.item.id});
    }

}

export default withRouter(VocabularyManagementItem);

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

