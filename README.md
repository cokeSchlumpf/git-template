# git-template
An incredible project.

## Preparing developer environment
For implementation purposes it is recommended to use Atom as primary code editor.

Please install the following additional packages into Atom:
- atom-beautify
- color-picker
- esformatter
- git-plus
- linter
- linter-eslint

## UI development cycle
To start the liberty profile with the backend application use Maven in project root directory.

```
mvn install
mvn integration-test -Dwatch
```

Afterwards start NPM watch.
```
cd src/main/webapp
npm install
npm run watch
```
Please note that if `mvn install` is executed then `npm install` is executed implicitly.

Now you're able to change the frontent-sources and they will be automatically pushed to the server such as IBM WebSphere Liberty Profile.

If you would like to call build steps separately then you should use the commands below.

Running build separately which includes format test, lint, babelify, css, and jsdoc build steps:
```
npm run build
```

Running build-dev separately which includes browserify and babelify build steps:
```
npm run build-dev
```

Running babelify separately:
```
npm run babelify
```

Running lessc separately:
```
npm run css
```

Running eslint separately:
```
npm run lint
```

Running code formatter separately:
```
npm run format
```

Running jsdoc:
```
npm run jsdoc
```

Running tests via jest:
```
npm test
```
