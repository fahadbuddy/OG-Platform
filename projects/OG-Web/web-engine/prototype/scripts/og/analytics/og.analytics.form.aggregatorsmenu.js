/*
 * Copyright 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 * Please see distribution for license.
 */
$.register_module({
    name: 'og.analytics.form.AggregatorsMenu',
    dependencies: [],
    obj: function () {
        var module = this, Block = og.common.util.ui.Block;
        var AggregatorsMenu = function (config) {
            if (!config) return og.dev.warn('og.analytics.AggregatorsMenu: Missing param [config] to constructor.');

            if (!(config.hasOwnProperty('form')) || !(config.form instanceof og.common.util.ui.Form))
                return og.dev.warn(
                    'og.analytics.AggregatorsMenu: Missing or invalid param key [config.form] to constructor.'
                );

            // Private
            var query = [], sel_val, sel_pos, $parent, form = config.form,
                aggregators = config.aggregators || [{idx:0}], initialized = false, $select,
                default_sel_txt = 'select aggregation...', del_s = '.og-icon-delete',
                options_s = '.OG-dropmenu-options', select_s = 'select', checkbox_s = '.og-option :checkbox';

            var add_row_handler = function (obj) {
                return new form.Block({
                    module: 'og.analytics.form_aggregation_row_tash',
                    generator : function (handler, tmpl, data) {
                        og.api.rest.aggregators.get().pipe(function (resp) {
                            data.aggregators = resp.data;
                            data.idx = obj.idx + 1;
                            handler(tmpl(data));
                        });
                    }
                });
            };

            var delete_handler = function (entry) {
                if (menu.opts.length === 1) {
                    $select.val(default_sel_txt).focus();
                    return remove_entry();
                }
                if($select !== undefined) menu.delete_handler($parent);
                for (var i = ~entry ? entry : sel_pos, len = query.length; i < len; query[i++].pos-=1);
                if (~entry) {
                    remove_entry(entry);
                }
            };

            var get_query = function () {
                return remove_orphans(), query.pluck('val');
            };

            var init = function () {
                menu = new og.analytics.form.DropMenu({cntr: $('.OG-analytics-form .og-aggregation')});
                if (menu.$dom) {
                    menu.$dom.toggle_prefix.append('<span>Aggregated by</span>');
                    if (menu.$dom.menu) {
                        $query = $('.aggregation-selection', menu.$dom.toggle);
                        menu.$dom.menu
                            .on('mousedown', 'input, button, div.og-icon-delete, a.OG-link-add', menu_handler)
                            .on('change', 'select', menu_handler);
                    }
                    menu.fire('initialized', [initialized = true]);
                }
            };

            var menu_handler = function (event) {
                var $elem = $(event.srcElement || event.target), entry;
                    $parent = $elem.parents(options_s);
                    $select = $parent.find(select_s);
                    sel_val = $select.val();
                    sel_pos = $parent.data('pos');
                    entry = query.pluck('pos').indexOf(sel_pos);
                if ($elem.is(menu.$dom.add)) return menu.stop(event), add_row_handler({idx:0}).html(function (html) {
                        menu.add_handler($(html));
                    });
                if ($elem.is(del_s)) return menu.stop(event), delete_handler(entry);
                if ($elem.is($select)) return select_handler(entry);
                if ($elem.is('button')) return menu.button_handler($elem.text());
            };

            var remove_entry = function (entry) {
                if (query.length === 1) return query.length = 0;
                query.splice(entry, 1);
            };

            var remove_orphans = function () {
                for (var i = menu.opts.length-1; i >= 0; i-=1) {
                    if (menu.opts.length === 1) break;
                    var option = menu.opts[i];
                    if ($(select_s, option).val() === default_sel_txt) menu.delete_handler(option);
                }
            };

            var reset = function () {
                for (var i = menu.opts.length-1; i >=0; i-=1) {
                    if (menu.opts.length === 1) {
                        menu.opts[i].val(default_sel_txt);
                        break;
                    }
                    init_menu_elems(i);
                    delete_handler(i);
                }
                return init_menu_elems(0), reset_query();
            };

            var select_handler = function (entry) {
                if (sel_val === default_sel_txt) {
                    remove_entry(entry);
                    if (query.length === 0) return $query.html(default_query_text);
                }
                else if (~entry) query[entry].val = sel_val;
                else query.splice(sel_pos, 0, {pos:sel_pos, val:sel_val, required_field:false});
            };

            form.Block.call(this, {
                data: { aggregators:[] },
                selector: '.og-aggregation',
                module: 'og.analytics.form_aggregation_tash',
                children: aggregators.map(add_row_handler),
                processor: function (data) {
                    data.aggregators = get_query();
                }
            });

            form.on('form:load', init);
        };

        AggregatorsMenu.prototype = new Block;

        return AggregatorsMenu;
    }
});