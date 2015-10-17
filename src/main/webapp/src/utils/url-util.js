export default {
  /*
   * @Deprecated Method is unsecure. Maybe we need a static configuration for this.
   */
  basePath() {
    let result = '/';
    const pathnames = window.location.pathname.split('/');

    if (pathnames.length > 1 && pathnames[1].length > 0) {
      result = '/' + pathnames[1];
    }

    return result;
  },

  /*
   *
   */
  baseURL() {
    return window.location.origin + this.basePath();
  }
};
