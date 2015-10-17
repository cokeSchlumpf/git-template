/**
 * View module.
 * @module components/view
 */

import React from 'react';
import { Link } from 'react-router';

/**
 * Utility function to create a React component.
 * @exports components/view
 */
export default React.createClass({
  displayName: 'View',

  propTypes: {
    children: React.PropTypes.any
  },

  render() {
    return (
      <div className="view">
        { this.props.children }
      </div>
      );
  }

});
