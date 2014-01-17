Date.weekdays = $w($MessageConst["Calendar.0"]);
Date.months = $w('1 2 3 4 5 6 7 8 9 10 11 12');

Date.first_day_of_week = 0;

Element.addMethods({
	build : function(element, type, options, style) {
		var newElement = new Element(type);
		$H(options).each(function(pair) {
			newElement[pair.key] = pair.value;
		});
		if (style)
			newElement.setStyle(style);
		element.appendChild(newElement);
		return newElement;
	}
});

Date.one_day = 24 * 60 * 60 * 1000;

Date.padded2 = function(hour) {
	var padded2 = parseInt(hour, 10);
	if (hour < 10)
		padded2 = "0" + padded2;
	return padded2;
};

Date.prototype.daysDistance = function(compare_date) {
	return Math.round((compare_date - this) / Date.one_day);
};

Math.floor_to_interval = function(n, i) {
	return Math.floor(n / i) * i;
};

SelectBox = Class.create({
	initialize : function(parent_element, values, html_options, style_options) {
		this.element = $(parent_element).build("select", html_options,
				style_options);
		this.populate(values);
	},

	populate : function(values) {
		this.element.update('');
		var that = this;
		$A(values).each(function(pair) {
			if (typeof (pair) != "object") {
				pair = [ pair, pair ];
			}

			that.element.build("option", {
				value : pair[1],
				innerHTML : pair[0]
			});
		});
	},

	setValue : function(value) {
		var e = this.element;
		var matched = false;
		$R(0, e.options.length - 1).each(function(i) {
			if (e.options[i].value == value.toString()) {
				e.selectedIndex = i;
				matched = true;
			}
		});
		return matched;
	},

	getValue : function() {
		return $F(this.element);
	}
});

CalendarDateSelect = Class.create();

CalendarDateSelect.prototype = {
	initialize : function(options) {
		this.options = {
			dateFormat : "yyyy-MM-dd",
			showTime : false,
			clearButton : true,
			yearRange : 50,
			minuteInterval : 1,
			embedded : null,
			onclose : null,
			onchange : null,
			oncheck : null
		};
		Object.extend(this.options, options || {});
	},

	show : function(inputField, dateFormat) {
		this.inputField = $(inputField);
		if (this.inputField && this.inputField.tagName != "INPUT")
			this.inputField = this.inputField.down("INPUT");
		if (!this.inputField) {
			return;
		}
		
		if (dateFormat) {
			this.options.dateFormat = dateFormat;
		}
		
		this.inputField.calendar_date_select = this;

		this.parseDate();
		this.initCalendarDiv();

		var embedded = $(this.options.embedded);
		if (!embedded) {
			Event.observe(document, "mousedown",
					this.closeIfClickedOut_handler = this.closeIfClickedOut
							.bindAsEventListener(this));
			Event.observe(document, "keypress", this.keyPress_handler = this.keyPress
					.bindAsEventListener(this));

			var p = $UI.getPopupOffsets(this.calendar_div);
			this.calendar_div.style.left = p[0] + "px";
			this.calendar_div.style.top = (p[1] + 1) + "px";
		}

		$Effect.show(this.calendar_div, {
			effects : this.options.effects
		});
	},

	initCalendarDiv : function() {
		if (!this.calendar_div) {
			var embedded = $(this.options.embedded);
			this.calendar_div = Element.build(embedded ? embedded : document.body,
					'div', {
						className : "calendar_date_select"
					}, embedded ? {} : {
						position : "absolute"
					}).hide();
	
			this.calendar_div.createDrag();
	
			var that = this;
			$w("top header body buttons footer bottom").each(
					function(name) {
						eval("var " + name + "_div = that." + name
								+ "_div = that.calendar_div.build('div', { className: 'cds_"
								+ name + "' }, { clear: 'left'} ); ");
					});
	
			this.initHeaderDiv();
			this.initButtonsDiv();
			this.initCalendarGrid();
		}
		this.refresh();
		this.setUseTime();
	},

	initHeaderDiv : function() {
		var header_div = this.header_div;
		this.next_month_button = header_div.build("a", {
			href : "###",
			onclick : function() {
				this.navMonth(this.date.getMonth() + 1);
				return false;
			}.bindAsEventListener(this),
			className : "next"
		});
		this.prev_month_button = header_div.build("a", {
			href : "###",
			onclick : function() {
				this.navMonth(this.date.getMonth() - 1);
				return false;
			}.bindAsEventListener(this),
			className : "prev"
		});

		this.year_select = new SelectBox(header_div, [], {
			className : "year",
			onchange : function() {
				this.navYear(this.year_select.getValue());
			}.bindAsEventListener(this)
		});
		this.populateYearRange();
		this.month_select = new SelectBox(header_div, $R(0, 11).map(function(m) {
			return [ Date.months[m], m ];
		}), {
			className : "month",
			onchange : function() {
				this.navMonth(this.month_select.getValue());
			}.bindAsEventListener(this)
		});
	},

	initCalendarGrid : function() {
		var body_div = this.body_div;
		this.calendar_day_grid = [];
		var days_table = body_div.build("table", {
			cellPadding : "0px",
			cellSpacing : "0px",
			width : "100%"
		});

		var weekdays_row = days_table.build("thead").build("tr");
		Date.weekdays.each(function(weekday) {
			weekdays_row.build("th", {
				innerHTML : weekday
			});
		});

		var days_tbody = days_table.build("tbody");

		var row_number = 0, weekday;
		for ( var cell_index = 0; cell_index < 42; cell_index++) {
			weekday = (cell_index + Date.first_day_of_week) % 7;
			if (cell_index % 7 == 0)
				days_row = days_tbody.build("tr", {
					className : 'row_' + row_number++
				});
			(this.calendar_day_grid[cell_index] = days_row.build("td", {
				calendar_date_select : this,
				onmouseover : function() {
					this.calendar_date_select.dayHover(this);
				},
				onmouseout : function() {
					this.calendar_date_select.dayHoverOut(this);
				},
				onclick : function() {
					this.calendar_date_select.updateSelectedDate(this, true);
				},
				className : (weekday == 0) || (weekday == 6) ? " weekend" : ""
			}, {
				cursor : "pointer"
			}));
		}
	},

	initButtonsDiv : function() {
		var buttons_div = this.buttons_div;
		if (this.options.showTime) {
			var blank_time = [];
			buttons_div.build("span", {
				className : "at_sign"
			});

			this.hour_select = new SelectBox(buttons_div, blank_time.concat($R(0, 23)
					.map(function(x) {
						return $A([ x, x ]);
					})), {
				calendar_date_select : this,
				onchange : function() {
					this.calendar_date_select.updateSelectedDate({
						hour : this.value
					});
					this.calendar_date_select.updateFooter();
				},
				className : "hour"
			});

			buttons_div.build("span", {
				innerHTML : ":",
				className : "seperator"
			});
			var that = this;
			this.minute_select = new SelectBox(buttons_div, blank_time.concat($R(0,
					59).select(function(x) {
				return (x % that.options.minuteInterval == 0);
			}).map(function(x) {
				return $A([ Date.padded2(x), x ]);
			})), {
				calendar_date_select : this,
				onchange : function() {
					this.calendar_date_select.updateSelectedDate({
						minute : this.value
					});
					this.calendar_date_select.updateFooter();
				},
				className : "minute"
			});
			buttons_div.build("br");
		}

		buttons_div.build("span", {
			innerHTML : "&nbsp;"
		});

		b = buttons_div.build("a", {
			innerHTML : $MessageConst["Calendar.1"],
			href : "###",
			onclick : function() {
				this.today();
				return false;
			}.bindAsEventListener(this)
		});

		if (this.options.clearButton) {
			buttons_div.build("span", {
				innerHTML : "&nbsp;|&nbsp;",
				className : "button_seperator"
			});
			buttons_div.build("a", {
				innerHTML : $MessageConst["Calendar.2"],
				href : "###",
				onclick : function() {
					this.clearDate();
					if (!this.embedded)
						this.close();
					return false;
				}.bindAsEventListener(this)
			});
		}
	},

	refresh : function() {
		this.refreshMonthYear();
		this.refreshCalendarGrid();

		this.setSelectedClass();
		this.updateFooter();
	},

	refreshCalendarGrid : function() {
		this.beginning_date = new Date(this.date).clearTime();
		this.beginning_date.setDate(1);
		this.beginning_date.setHours(12);

		var pre_days = this.beginning_date.getDay();

		if (pre_days < 3)
			pre_days += 7;
		this.beginning_date.setDate(1 - pre_days + Date.first_day_of_week);

		var iterator = new Date(this.beginning_date);

		var today = new Date().clearTime();
		var this_month = this.date.getMonth();
		vdc = this.options.oncheck;
		for ( var cell_index = 0; cell_index < 42; cell_index++) {
			day = iterator.getDate();
			month = iterator.getMonth();
			cell = this.calendar_day_grid[cell_index];
			cell.innerHTML = day;

			if (month != this_month)
				cell.addClassName("other");
			else 
				cell.removeClassName("other");

			cell.day = day;
			cell.month = month;
			cell.year = iterator.getFullYear();
			if (vdc) {
				if (vdc(iterator.clearTime()))
					cell.removeClassName("disabled");
				else
					cell.addClassName("disabled");
			}
			iterator.setDate(day + 1);
		}

		if (this.today_cell)
			this.today_cell.removeClassName("today");

		if ($R(0, 41).include(
				days_until = this.beginning_date.clearTime().daysDistance(today))) {
			this.today_cell = this.calendar_day_grid[days_until];
			this.today_cell.addClassName("today");
		}
	},

	refreshMonthYear : function() {
		var m = this.date.getMonth();
		var y = this.date.getFullYear();

		this.month_select.setValue(m, false);

		var e = this.year_select.element;
		if (this.flexibleYearRange()
				&& (!(this.year_select.setValue(y, false)) || e.selectedIndex <= 1 || e.selectedIndex >= e.options.length - 2))
			this.populateYearRange();

		this.year_select.setValue(y);
	},

	populateYearRange : function() {
		this.year_select.populate(this.yearRange().toArray());
	},

	yearRange : function() {
		if (!this.flexibleYearRange())
			return $R(this.options.yearRange[0], this.options.yearRange[1]);

		var y = this.date.getFullYear();
		return $R(y - this.options.yearRange, y + this.options.yearRange);
	},

	flexibleYearRange : function() {
		return (typeof (this.options.yearRange) == "number");
	},

	validYear : function(year) {
		if (this.flexibleYearRange()) {
			return true;
		} else {
			return this.yearRange().include(year);
		}
	},

	dayHover : function(element) {
		var hover_date = new Date(this.selected_date);
		hover_date.setYear(element.year);
		hover_date.setMonth(element.month);
		hover_date.setDate(element.day);
		this.updateFooter(hover_date.format(this.options.dateFormat));
	},

	dayHoverOut : function(element) {
		this.updateFooter();
	},

	clearSelectedClass : function() {
		if (this.selected_cell)
			this.selected_cell.removeClassName("selected");
	},

	setSelectedClass : function() {
		if (!this.selection_made)
			return;
		this.clearSelectedClass();
		if ($R(0, 42).include(
				days_until = this.beginning_date.clearTime().daysDistance(
						this.selected_date.clearTime()))) {
			this.selected_cell = this.calendar_day_grid[days_until];
			this.selected_cell.addClassName("selected");
		}
	},

	reparse : function() {
		this.parseDate();
		this.refresh();
	},

	dateString : function() {
		return (this.selection_made) ? this.selected_date
				.format(this.options.dateFormat) : "&nbsp;";
	},

	parseDate : function() {
		var value = $F(this.inputField).strip();
		this.selection_made = (value != "");
		this.date = Date.parseString(this.options.date || value,
				this.options.dateFormat);
		if (!this.date)
			this.date = new Date();
		if (!this.validYear(this.date.getFullYear()))
			this.date
					.setYear((this.date.getFullYear() < this.yearRange().start) ? this
							.yearRange().start : this.yearRange().end);
		this.selected_date = new Date(this.date);
	},

	updateFooter : function(text) {
		if (!text)
			text = this.dateString();
		this.footer_div.update('');
		this.footer_div.build("span", {
			innerHTML : text
		});
	},

	clearDate : function() {
		var last_value = this.inputField.value;
		this.inputField.value = "";
		this.clearSelectedClass();
		this.updateFooter('&nbsp;');
		if (last_value != this.inputField.value)
			this.callback("onchange");
	},

	updateSelectedDate : function(partsOrElement, via_click) {
		var parts = $H(partsOrElement);
		if (parts.get("day")) {
			var t_selected_date = this.selected_date, vdc = this.options.oncheck;
//			for ( var x = 0; x <= 3; x++)
			t_selected_date.setDate(parts.get("day"));
			t_selected_date.setYear(parts.get("year"));
			t_selected_date.setMonth(parts.get("month"));

			if (vdc && !vdc(t_selected_date.clearTime())) {
				return false;
			}
			this.selected_date = t_selected_date;
			this.selection_made = true;
		}

		if (!isNaN(parts.get("hour")))
			this.selected_date.setHours(parts.get("hour"));
		if (!isNaN(parts.get("minute")))
			this.selected_date.setMinutes(Math.floor_to_interval(parts.get("minute"),
					this.options.minuteInterval));

		if (this.selection_made)
			this.updateValue();
		if (via_click && this.closeOnClick(t_selected_date.clearTime())) {
			this.close();
		}
	},

	closeOnClick : function(time) {
		if (this.embedded)
			return false;
		if (this.options.onclose === null)
			return true;
		else
			return this.options.onclose(time);
	},

	navMonth : function(month) {
		(target_date = new Date(this.date)).setMonth(month);
		return (this.navTo(target_date));
	},

	navYear : function(year) {
		(target_date = new Date(this.date)).setYear(year);
		return (this.navTo(target_date));
	},

	navTo : function(date) {
		if (!this.validYear(date.getFullYear()))
			return false;
		this.date = date;
		this.updateSelectedDate({
			day : date.getDate(),
			month : date.getMonth(),
			year : date.getFullYear()
		}, false);
		this.refresh();
		this.callback("after_navigate", this.date);
		return true;
	},

	setUseTime : function() {
		if (this.options.showTime && this.selected_date) {
			var minute = Math.floor_to_interval(this.selected_date.getMinutes(),
					this.options.minuteInterval);
			var hour = this.selected_date.getHours();

			this.hour_select.setValue(hour);
			this.minute_select.setValue(minute);
		}
	},

	updateValue : function() {
		var last_value = this.inputField.value;
		this.inputField.value = this.dateString();
		if (last_value != this.inputField.value)
			this.callback("onchange");
	},

	today : function() {
		var d = new Date();
		this.date = d;
		this.updateSelectedDate({
			day : d.getDate(),
			month : d.getMonth(),
			year : d.getFullYear(),
			hour : d.getHours(),
			minute : d.getMinutes()
		}, false);
		this.refresh();
	},

	close : function() {
		Event.stopObserving(document, "mousedown", this.closeIfClickedOut_handler);
		Event.stopObserving(document, "keypress", this.keyPress_handler);
		this.inputField.calendar_date_select = null;
		$Effect.hide(this.calendar_div, {
			effects : this.options.effects
		});
		try {
			this.inputField.focus();
		} catch (e) {
		}
	},

	closeIfClickedOut : function(e) {
		if (!$(Event.element(e)).descendantOf(this.calendar_div))
			this.close();
	},

	keyPress : function(e) {
		if (e.keyCode == Event.KEY_ESC)
			this.close();
	},

	callback : function(name, param) {
		if (this.options[name]) {
			this.options[name].bind(this.inputField)(param);
		}
	}
};