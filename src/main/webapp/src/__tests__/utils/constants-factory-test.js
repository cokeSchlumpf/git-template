jest.dontMock('../../utils/constants-factory');
jest.mock('../../utils/array-util');

describe('constantsFactory', function() {
  it('creates an object with constants from a list of strings (array)', function() {
    const constantsFactory = require('../../utils/constants-factory');
    const arrayUtil = require('../../utils/array-util');
    arrayUtil.isArray.mockReturnValue(true);
    expect(constantsFactory([ 'A', 'B', 'C' ])).toEqual({
      A: 'A',
      B: 'B',
      C: 'C'
    });
  });

  it('creates an object with constants from list of strings (arguments)', function() {
    const constantsFactory = require('../../utils/constants-factory');
    const arrayUtil = require('../../utils/array-util');
    arrayUtil.isArray.mockReturnValue(false);
    expect(constantsFactory('A', 'B', 'C')).toEqual({
      A: 'A',
      B: 'B',
      C: 'C'
    });
  });
});
