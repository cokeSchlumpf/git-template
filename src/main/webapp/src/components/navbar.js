/**
 * Navbar module.
 * @module components/navbar
 */

import React from 'react';
import { Link as RouterLink, IndexLink } from 'react-router';
import { Grid } from 'react-bootstrap';

export default React.createClass({
  displayName: 'Navbar',

  render() {
    return (
      <nav className="navbar navbar-static-top">
        <Grid>
          <div className="navbar-header">
            <button type="button"
              className="navbar-toggle collapsed"
              data-toggle="collapse"
              data-target="#navbar"
              aria-expanded="false"
              aria-controls="navbar">
              <span className="sr-only">Toggle navigation</span>
              <span className="icon-bar"></span>
              <span className="icon-bar"></span>
              <span className="icon-bar"></span>
            </button>
            <IndexLink className="navbar-brand" to="/">
              git-template <span className="highlight">clone</span>
            </IndexLink>
          </div>
          <div id="navbar" className="collapse navbar-collapse">
            <ul className="nav navbar-nav pull-right">
            </ul>
          </div>
        </Grid>
      </nav>
      );
  }
});
