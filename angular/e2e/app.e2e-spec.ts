import { EstimationToolTemplatePage } from './app.po';

describe('EstimationTool App', function() {
  let page: EstimationToolTemplatePage;

  beforeEach(() => {
    page = new EstimationToolTemplatePage();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('app works!');
  });
});
