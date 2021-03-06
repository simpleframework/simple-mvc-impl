var $pager_action = function(item) {
  item = $Target(item);
  var act = (item.hasClassName("pager") ? item : item.up(".pager")).action;
  act.currentRow = item.hasClassName("titem") ? item : item.up(".titem");
  return act;
};

function pager_autoload(mmore) {
  var top = mmore.cumulativeOffset().top;
  Event.observe(window, "scroll", function() {
    var scrollTop = $UI.scrollTop();
    var _top = top || mmore._top;
    if (!_top) {
      return;
    }
    if (scrollTop + document.viewport.getHeight() > _top) {
      mmore._top = null;
      top = null;
      mmore.simulate("click");
    }
  });
};