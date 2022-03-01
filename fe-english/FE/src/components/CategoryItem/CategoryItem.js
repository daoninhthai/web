import React, {Component} from 'react'
import './CategoryItem.css'
import vocabularyIcon from '../../resources/vocabulary_icon.png'

class CategoryItem extends Component{
    constructor(props){
        super(props);
    }

    render() {
        const {item} = this.props;
        return (
            <div className = "CategoryItem">
                <a className="Link" href = {item.src}>
                    <i className = "Icon">
                        <img className="Icon_Image" src={vocabularyIcon}></img>
                    </i>
                    <h4 className="Title">{item.name ? item.name : "Home"}</h4>
                </a>
                <p className="Description">
                    {item.description ? item.description : "Description"}
                </p>
            </div>
        );
    }
}

export default CategoryItem;

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

