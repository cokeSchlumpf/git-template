export default {
  /**
   * Checks whether the defined object is an array or not.
   * @param {object} obj the defined object.
   * @return {boolean} true if the defined object is an array.
   */
  isArray(obj) {
    return Object.prototype.toString.call(obj) === '[object Array]';
  },

  /**
   * Makes a string defined by an array, a separator, a prefixed string,
   * and a suffixed string.
   * @param {array} array the defined array of elements.
   * @param {string} separator the defined separator.
   * @param {string} before the defined prefixed string.
   * @param {string} after the defined suffixed string.
   * @return {string} a string representation of the defined parameters.
   */
  mkString(array, separator = ', ', before = '', after = '') {
    const result = array.reduce((concat, item, index) => {
      let newResult = concat;
      if (index > 0) {
        newResult = newResult + separator;
      }

      return newResult + item;
    }, before);

    return result + after;
  },

  fromString(string, separator = ',', stripWhitespace = true) {
    return string.split(separator).map(s => {
      return stripWhitespace ? s.replace(/ /g, '') : s;
    });
  }
};
