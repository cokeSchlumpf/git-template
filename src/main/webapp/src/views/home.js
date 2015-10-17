import React from 'react';
import { Client } from 'node-rest-client';
import { ButtonInput, Input, Grid, Col, Row, Jumbotron } from 'react-bootstrap';

import update from 'react-addons-update';
import cx from 'classnames';

import ArrayUtil from '../utils/array-util';
import UrlUtil from '../utils/url-util';
import View from '../components/view';

const Header = React.createClass({
  displayName: 'Home.Header',

  render() {
    return (
      <Jumbotron>
        <Grid>
          <Row>
            <Col md={ 4 } />
            <Col md={ 1 } />
            <Col md={ 7 } />
          </Row>
        </Grid>
      </Jumbotron>
      );
  }
});

const H2 = React.createClass({
  displayName: 'H2',

  propTypes: {
    title: React.PropTypes.string
  },

  render() {
    return (
      <Row>
        <Col md={ 12 }>
          <h2>{ this.props.title }</h2>
        </Col>
      </Row>);
  }
});

const VariableInput = React.createClass({
  displayName: 'VariableInput',

  propTypes: {
    name: React.PropTypes.string,
    value: React.PropTypes.string,
    onChange: React.PropTypes.func,
    onRemove: React.PropTypes.func
  },

  handleNameChange(event) {
    this.props.onChange(event.target.value, this.props.value);
  },

  handleValueChange(event) {
    this.props.onChange(this.props.name, event.target.value);
  },

  render() {
    return (
      <div className="form-group">
        <div className="col-xs-2">
          <input className="form-control" type="text" value={ this.props.name } onChange={ this.handleNameChange } placehoder="Variable" />
        </div>
        <div className="col-xs-8">
          <input className="form-control" type="text" value={ this.props.value } onChange={ this.handleValueChange } placeholder="Value" />
        </div>
        <div className="col-xs-2">
          <ButtonInput className="form-button remove" value="Remove" onClick={ this.props.onRemove } />
        </div>
      </div>);
  }
});

export default React.createClass({
  displayName: 'Home',

  getInitialState() {
    return {
      username: 'cokeSchlumpf',
      repository: 'template-test',
      ref: 'refs/head/master',
      rootPath: '/',
      files: '*.js, package.json, pom.xml',
      encoding: 'UTF-8',
      variables: [
        {
          name: 'project-name',
          value: 'your-project-name'
        },
        {
          name: 'version',
          value: '1.0'
        }
      ]
    };
  },

  addVariable() {
    this.setState({
      variables: update(this.state.variables, {
        $push: [ {
          name: 'Variable',
          value: 'Value'
        } ]
      })
    });
  },

  handleChange(field) {
    const self = this;

    return function(event) {
      self.setState({
        [field]: event.target.value
      });
    };
  },

  handleFetch() {
    const inData = {
      username: this.state.username,
      repository: this.state.repository
    };

    const args = {
      data: inData,
      headers: {
        'Content-Type': 'application/json'
      }
    };

    const serviceUrl = `${UrlUtil.baseURL()}/api/templates`;
    const client = new Client();

    client.post(serviceUrl, args, (outData, response) => {
      console.log(outData);

      this.setState(update(outData, {
        files: {
          $set: ArrayUtil.mkString(outData.files)
        }
      }));
    });
  },

  handleSubmit() {
    const inData = update(this.state, {
      files: {
        $set: ArrayUtil.fromString(this.state.files)
      }
    });

    const args = {
      data: inData,
      headers: {
        'Content-Type': 'application/json'
      }
    };

    const serviceUrl = `${UrlUtil.baseURL()}/api/templates`;
    const client = new Client();

    client.put(serviceUrl, args, (outData, response) => {
      document.getElementById('download').src = outData.url;
    });
  },

  handleVariableChange(index) {
    const self = this;

    return function(name, value) {
      self.setState({
        variables: update(self.state.variables, {
          $splice: [ [ index, 1, {
            name: name,
            value: value
          } ] ]
        })
      });
    };
  },

  handleVariableRemove(index) {
    const self = this;

    return function(name, value) {
      self.setState({
        variables: update(self.state.variables, {
          $splice: [ [ index, 1 ] ]
        })
      });
    };
  },

  render() {
    const self = this;
    const renderVariable = (variable, index) => {
      return (
        <VariableInput key={ 'var_' + index } {...variable} onChange={ self.handleVariableChange(index) } onRemove={ self.handleVariableRemove(index) } />);
    };

    return (
      <View>
        <Header />
        <Grid className="content">
          <H2 title="Template GitHub Repository" />
          <Row>
            <Col xs={ 12 } md={ 5 }>
              <Input value={ this.state.username } onChange={ this.handleChange('username') } type="text" label="Git Username" placeholder="Enter the Username of the repository's owner." />
            </Col>
            <Col xs={ 12 } md={ 5 }>
              <Input value={ this.state.repository } onChange={ this.handleChange('repository') } type="text" label="Repository" placeholder="Enter the name of the repository." />
            </Col>
            <Col xs={ 12 } md={ 2 }>
              <ButtonInput className="form-button" value="Fetch Variables" onClick={ this.handleFetch } />
            </Col>
          </Row>
          <H2 title="Template Configuration" />
          <Row>
            <Col className="form-horizontal config">
              <Input value={ this.state.ref }
                onChange={ this.handleChange('ref') }
                type="text"
                label="Reference"
                help="The reference for the tag or the branch you are going to copy."
                labelClassName="col-xs-2"
                wrapperClassName="col-xs-10" />
              <Input value={ this.state.rootPath }
                onChange={ this.handleChange('rootPath') }
                type="text"
                label="Path"
                help="The path within the repository which should be copied."
                labelClassName="col-xs-2"
                wrapperClassName="col-xs-10" />
              <Input value={ this.state.files }
                onChange={ this.handleChange('files') }
                type="text"
                label="Filter files pattern."
                help="Select files which should be used for search and replace variables, separated by comma. E.g.: *.js, package.json."
                labelClassName="col-xs-2"
                wrapperClassName="col-xs-10" />
              <Input value={ this.state.encoding }
                onChange={ this.handleChange('encoding') }
                type="text"
                label="Encoding"
                help="The encoding of your files."
                labelClassName="col-xs-2"
                wrapperClassName="col-xs-10" />
            </Col>
          </Row>
          <H2 title="Variables" />
          <Row>
            <Col md={ 12 } className="form-horizontal variables">
              { this.state.variables.map(renderVariable) }
            </Col>
          </Row>
          <Row>
            <Col md={ 8 } />
            <Col md={ 2 }>
              <ButtonInput className="form-button" value="Add Variable" onClick={ this.addVariable } />
            </Col>
            <Col md={ 2 }>
              <ButtonInput type="submit" className="form-button" value="Get Template" bsStyle="primary" onClick={ this.handleSubmit } />
            </Col>
          </Row>
        </Grid>
      </View>);
  }
});
