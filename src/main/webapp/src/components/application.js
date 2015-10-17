import React from 'react';
import { Router, Route, IndexRoute } from 'react-router';
import { createHistory, useBasename } from 'history';

import Home from '../views/home';

import Navbar from './navbar';

const App = React.createClass({
  displayName: 'App',

  propTypes: {
    children: React.PropTypes.any
  },

  render() {
    return (
      <div>
        <Navbar />
        { this.props.children }
      </div>
      );
  }
});

export default (
<Router>
  <Route path="/" component={ App }>
    <IndexRoute component={ Home } name="home" />
  </Route>
</Router>
);
