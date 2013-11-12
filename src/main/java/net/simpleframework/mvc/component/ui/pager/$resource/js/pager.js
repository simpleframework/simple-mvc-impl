
var $pager_action = function(item) {
	item = $Target(item);
	var act = (item.hasClassName("pager") ? item : item.up(".pager")).action;
	act.currentRow = item.hasClassName("titem") ? item : item.up(".titem");
	return act;
};