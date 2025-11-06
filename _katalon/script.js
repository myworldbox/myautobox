window.conditionalClick = function(selector) {
  var el = document.querySelector(selector);
  if (el && el.offsetParent !== null) {
    el.click();
  }
};