/*!
 * Metro UI CSS v3.0.0 (http://metroui.org.ua)
 * Copyright 2012-2015 Sergey Pimenov
 * Licensed under MIT (http://metroui.org.ua/license.html)
 */
if (typeof jQuery === 'undefined') {
    throw new Error('Metro\'s JavaScript requires jQuery');
}

window.METRO_VERSION = '3.0.0';
window.METRO_AUTO_REINIT = true;
window.METRO_LANGUAGE = 'en';
window.METRO_LOCALE = 'EN_en';
window.METRO_CURRENT_LOCALE = 'en';
window.METRO_SHOW_TYPE = 'slide';
window.METRO_DEBUG = true;

window.canObserveMutation = 'MutationObserver' in window;

String.prototype.isUrl = function(){
    "use strict";
    var regexp = /^(ftp|http|https):\/\/(\w+:{0,1}\w*@)?(\S+)(:[0-9]+)?(\/|\/([\w#!:.?+=&%@!\-\/]))?/;
    return regexp.test(this);
};

String.prototype.isColor = function(){
    "use strict";
    return /(^#[0-9A-F]{6}$)|(^#[0-9A-F]{3}$)/i.test(this);
};

window.uniqueId = function (prefix){
    "use strict";
    return (prefix || 'id') + ((new Date()).getTime());
};


/*
 * Date Format 1.2.3
 * (c) 2007-2009 Steven Levithan <stevenlevithan.com>
 * MIT license
 *
 * Includes enhancements by Scott Trenda <scott.trenda.net>
 * and Kris Kowal <cixar.com/~kris.kowal/>
 *
 * Accepts a date, a mask, or a date and a mask.
 * Returns a formatted version of the given date.
 * The date defaults to the current date/time.
 * The mask defaults to dateFormat.masks.default.
 */
// this is a temporary solution

var dateFormat = function () {

    "use strict";

    var	token = /d{1,4}|m{1,4}|yy(?:yy)?|([HhMsTt])\1?|[LloSZ]|"[^"]*"|'[^']*'/g,
        timezone = /\b(?:[PMCEA][SDP]T|(?:Pacific|Mountain|Central|Eastern|Atlantic) (?:Standard|Daylight|Prevailing) Time|(?:GMT|UTC)(?:[-+]\d{4})?)\b/g,
        timezoneClip = /[^-+\dA-Z]/g,
        pad = function (val, len) {
            val = String(val);
            len = len || 2;
            while (val.length < len) {val = "0" + val;}
            return val;
        };

    // Regexes and supporting functions are cached through closure
    return function (date, mask, utc) {
        var dF = dateFormat;

        // You can't provide utc if you skip other args (use the "UTC:" mask prefix)
        if (arguments.length === 1 && Object.prototype.toString.call(date) === "[object String]" && !/\d/.test(date)) {
            mask = date;
            date = undefined;
        }

        //console.log(arguments);

        // Passing date through Date applies Date.parse, if necessary
        date = date ? new Date(date) : new Date();
        //if (isNaN(date)) throw SyntaxError("invalid date");

        mask = String(dF.masks[mask] || mask || dF.masks["default"]);

        // Allow setting the utc argument via the mask
        if (mask.slice(0, 4) === "UTC:") {
            mask = mask.slice(4);
            utc = true;
        }

        //console.log(locale);

        var locale = window.METRO_CURRENT_LOCALE || 'en';

        var	_ = utc ? "getUTC" : "get",
            d = date[_ + "Date"](),
            D = date[_ + "Day"](),
            m = date[_ + "Month"](),
            y = date[_ + "FullYear"](),
            H = date[_ + "Hours"](),
            M = date[_ + "Minutes"](),
            s = date[_ + "Seconds"](),
            L = date[_ + "Milliseconds"](),
            o = utc ? 0 : date.getTimezoneOffset(),
            flags = {
                d:    d,
                dd:   pad(d),
                ddd:  window.METRO_LOCALES[locale].days[D],
                dddd: window.METRO_LOCALES[locale].days[D + 7],
                m:    m + 1,
                mm:   pad(m + 1),
                mmm: window.METRO_LOCALES[locale].months[m],
                mmmm: window.METRO_LOCALES[locale].months[m + 12],
                yy:   String(y).slice(2),
                yyyy: y,
                h:    H % 12 || 12,
                hh:   pad(H % 12 || 12),
                H:    H,
                HH:   pad(H),
                M:    M,
                MM:   pad(M),
                s:    s,
                ss:   pad(s),
                l:    pad(L, 3),
                L:    pad(L > 99 ? Math.round(L / 10) : L),
                t:    H < 12 ? "a"  : "p",
                tt:   H < 12 ? "am" : "pm",
                T:    H < 12 ? "A"  : "P",
                TT:   H < 12 ? "AM" : "PM",
                Z:    utc ? "UTC" : (String(date).match(timezone) || [""]).pop().replace(timezoneClip, ""),
                o:    (o > 0 ? "-" : "+") + pad(Math.floor(Math.abs(o) / 60) * 100 + Math.abs(o) % 60, 4),
                S:    ["th", "st", "nd", "rd"][d % 10 > 3 ? 0 : (d % 100 - d % 10 !== 10) * d % 10]
            };

        return mask.replace(token, function ($0) {
            return $0 in flags ? flags[$0] : $0.slice(1, $0.length - 1);
        });
    };
}();

// Some common format strings
dateFormat.masks = {
    "default":      "ddd mmm dd yyyy HH:MM:ss",
    shortDate:      "m/d/yy",
    mediumDate:     "mmm d, yyyy",
    longDate:       "mmmm d, yyyy",
    fullDate:       "dddd, mmmm d, yyyy",
    shortTime:      "h:MM TT",
    mediumTime:     "h:MM:ss TT",
    longTime:       "h:MM:ss TT Z",
    isoDate:        "yyyy-mm-dd",
    isoTime:        "HH:MM:ss",
    isoDateTime:    "yyyy-mm-dd'T'HH:MM:ss",
    isoUtcDateTime: "UTC:yyyy-mm-dd'T'HH:MM:ss'Z'"
};

// For convenience...
Date.prototype.format = function (mask, utc) {
    "use strict";
    return dateFormat(this, mask, utc);
};

/*
 * End date format
 */


(function($){
    "use strict";

    $.fn.reverse = Array.prototype.reverse;

    $.Metro = function(params){
        params = $.extend({
        }, params);
    };

    $.Metro.initWidgets = function(){
        var widgets;
        widgets = $("[data-role]");
        $.each(widgets, function(){
            var $this = $(this);
            var roles = $this.data('role').split(/\s*,\s*/);
            roles.map(function(func){
                try {
                    if ($.fn[func] !== undefined) {$.fn[func].call($this);}
                } catch(e) {
                    if (window.METRO_DEBUG) {
                        console.log(e.message, e.stack);
                    }
                }
            });
        });
    };
})(jQuery);

$(function(){
    "use strict";

    $.Metro.initWidgets();

    if (window.METRO_AUTO_REINIT) {
        if (!window.canObserveMutation) {
            var originalDOM = $('body').html(),
                actualDOM;

            setInterval(function () {
                actualDOM = $('body').html();

                if (originalDOM !== actualDOM) {
                    originalDOM = actualDOM;

                    $.Metro.initWidgets();
                }
            }, 100);
        } else {
            var observer, observerOptions, observerCallback;
            observerOptions = {
                'childList': true,
                'subtree': true
            };
            observerCallback = function(mutations){
                mutations.map(function(record){
                    if (record.addedNodes) {

                        /*jshint loopfunc: true */
                        var obj, widgets, plugins;

                        for(var i = 0, l = record.addedNodes.length; i < l; i++) {
                            obj = $(record.addedNodes[i]);
                            plugins = obj.find("[data-role]");

                            if (obj.data('role') !== undefined) {
                                widgets = $.merge(plugins, obj);
                            } else {
                                widgets = plugins;
                            }

                            if (widgets.length) {
                                $.each(widgets, function(){
                                    var $this = $(this);
                                    var roles = $this.data('role').split(/\s*,\s*/);
                                    roles.map(function(func){
                                        try {
                                            if ($.fn[func] !== undefined) {
                                                $.fn[func].call($this);
                                            }
                                        } catch(e) {
                                            if (window.METRO_DEBUG) {
                                                console.log(e.message, e.stack);
                                            }
                                        }
                                    });
                                });
                            }
                        }
                    }
                });
            };
            observer = new MutationObserver(observerCallback);
            observer.observe(document, observerOptions);
        }
    }
});

/*! jQuery UI - v1.11.3 - 2015-02-23
* http://jqueryui.com
* Includes: widget.js
* Copyright 2015 jQuery Foundation and other contributors; Licensed MIT */

(function( factory ) {
	if ( typeof define === "function" && define.amd ) {

		// AMD. Register as an anonymous module.
		define([ "jquery" ], factory );
	} else {

		// Browser globals
		factory( jQuery );
	}
}(function( $ ) {
/*!
 * jQuery UI Widget 1.11.3
 * http://jqueryui.com
 *
 * Copyright jQuery Foundation and other contributors
 * Released under the MIT license.
 * http://jquery.org/license
 *
 * http://api.jqueryui.com/jQuery.widget/
 */


var widget_uuid = 0,
	widget_slice = Array.prototype.slice;

$.cleanData = (function( orig ) {
	return function( elems ) {
		var events, elem, i;
		for ( i = 0; (elem = elems[i]) != null; i++ ) {
			try {

				// Only trigger remove when necessary to save time
				events = $._data( elem, "events" );
				if ( events && events.remove ) {
					$( elem ).triggerHandler( "remove" );
				}

			// http://bugs.jquery.com/ticket/8235
			} catch ( e ) {}
		}
		orig( elems );
	};
})( $.cleanData );

$.widget = function( name, base, prototype ) {
	var fullName, existingConstructor, constructor, basePrototype,
		// proxiedPrototype allows the provided prototype to remain unmodified
		// so that it can be used as a mixin for multiple widgets (#8876)
		proxiedPrototype = {},
		namespace = name.split( "." )[ 0 ];

	name = name.split( "." )[ 1 ];
	fullName = namespace + "-" + name;

	if ( !prototype ) {
		prototype = base;
		base = $.Widget;
	}

	// create selector for plugin
	$.expr[ ":" ][ fullName.toLowerCase() ] = function( elem ) {
		return !!$.data( elem, fullName );
	};

	$[ namespace ] = $[ namespace ] || {};
	existingConstructor = $[ namespace ][ name ];
	constructor = $[ namespace ][ name ] = function( options, element ) {
		// allow instantiation without "new" keyword
		if ( !this._createWidget ) {
			return new constructor( options, element );
		}

		// allow instantiation without initializing for simple inheritance
		// must use "new" keyword (the code above always passes args)
		if ( arguments.length ) {
			this._createWidget( options, element );
		}
	};
	// extend with the existing constructor to carry over any static properties
	$.extend( constructor, existingConstructor, {
		version: prototype.version,
		// copy the object used to create the prototype in case we need to
		// redefine the widget later
		_proto: $.extend( {}, prototype ),
		// track widgets that inherit from this widget in case this widget is
		// redefined after a widget inherits from it
		_childConstructors: []
	});

	basePrototype = new base();
	// we need to make the options hash a property directly on the new instance
	// otherwise we'll modify the options hash on the prototype that we're
	// inheriting from
	basePrototype.options = $.widget.extend( {}, basePrototype.options );
	$.each( prototype, function( prop, value ) {
		if ( !$.isFunction( value ) ) {
			proxiedPrototype[ prop ] = value;
			return;
		}
		proxiedPrototype[ prop ] = (function() {
			var _super = function() {
					return base.prototype[ prop ].apply( this, arguments );
				},
				_superApply = function( args ) {
					return base.prototype[ prop ].apply( this, args );
				};
			return function() {
				var __super = this._super,
					__superApply = this._superApply,
					returnValue;

				this._super = _super;
				this._superApply = _superApply;

				returnValue = value.apply( this, arguments );

				this._super = __super;
				this._superApply = __superApply;

				return returnValue;
			};
		})();
	});
	constructor.prototype = $.widget.extend( basePrototype, {
		// TODO: remove support for widgetEventPrefix
		// always use the name + a colon as the prefix, e.g., draggable:start
		// don't prefix for widgets that aren't DOM-based
		widgetEventPrefix: existingConstructor ? (basePrototype.widgetEventPrefix || name) : name
	}, proxiedPrototype, {
		constructor: constructor,
		namespace: namespace,
		widgetName: name,
		widgetFullName: fullName
	});

	// If this widget is being redefined then we need to find all widgets that
	// are inheriting from it and redefine all of them so that they inherit from
	// the new version of this widget. We're essentially trying to replace one
	// level in the prototype chain.
	if ( existingConstructor ) {
		$.each( existingConstructor._childConstructors, function( i, child ) {
			var childPrototype = child.prototype;

			// redefine the child widget using the same prototype that was
			// originally used, but inherit from the new version of the base
			$.widget( childPrototype.namespace + "." + childPrototype.widgetName, constructor, child._proto );
		});
		// remove the list of existing child constructors from the old constructor
		// so the old child constructors can be garbage collected
		delete existingConstructor._childConstructors;
	} else {
		base._childConstructors.push( constructor );
	}

	$.widget.bridge( name, constructor );

	return constructor;
};

$.widget.extend = function( target ) {
	var input = widget_slice.call( arguments, 1 ),
		inputIndex = 0,
		inputLength = input.length,
		key,
		value;
	for ( ; inputIndex < inputLength; inputIndex++ ) {
		for ( key in input[ inputIndex ] ) {
			value = input[ inputIndex ][ key ];
			if ( input[ inputIndex ].hasOwnProperty( key ) && value !== undefined ) {
				// Clone objects
				if ( $.isPlainObject( value ) ) {
					target[ key ] = $.isPlainObject( target[ key ] ) ?
						$.widget.extend( {}, target[ key ], value ) :
						// Don't extend strings, arrays, etc. with objects
						$.widget.extend( {}, value );
				// Copy everything else by reference
				} else {
					target[ key ] = value;
				}
			}
		}
	}
	return target;
};

$.widget.bridge = function( name, object ) {
	var fullName = object.prototype.widgetFullName || name;
	$.fn[ name ] = function( options ) {
		var isMethodCall = typeof options === "string",
			args = widget_slice.call( arguments, 1 ),
			returnValue = this;

		if ( isMethodCall ) {
			this.each(function() {
				var methodValue,
					instance = $.data( this, fullName );
				if ( options === "instance" ) {
					returnValue = instance;
					return false;
				}
				if ( !instance ) {
					return $.error( "cannot call methods on " + name + " prior to initialization; " +
						"attempted to call method '" + options + "'" );
				}
				if ( !$.isFunction( instance[options] ) || options.charAt( 0 ) === "_" ) {
					return $.error( "no such method '" + options + "' for " + name + " widget instance" );
				}
				methodValue = instance[ options ].apply( instance, args );
				if ( methodValue !== instance && methodValue !== undefined ) {
					returnValue = methodValue && methodValue.jquery ?
						returnValue.pushStack( methodValue.get() ) :
						methodValue;
					return false;
				}
			});
		} else {

			// Allow multiple hashes to be passed on init
			if ( args.length ) {
				options = $.widget.extend.apply( null, [ options ].concat(args) );
			}

			this.each(function() {
				var instance = $.data( this, fullName );
				if ( instance ) {
					instance.option( options || {} );
					if ( instance._init ) {
						instance._init();
					}
				} else {
					$.data( this, fullName, new object( options, this ) );
				}
			});
		}

		return returnValue;
	};
};

$.Widget = function( /* options, element */ ) {};
$.Widget._childConstructors = [];

$.Widget.prototype = {
	widgetName: "widget",
	widgetEventPrefix: "",
	defaultElement: "<div>",
	options: {
		disabled: false,

		// callbacks
		create: null
	},
	_createWidget: function( options, element ) {
		element = $( element || this.defaultElement || this )[ 0 ];
		this.element = $( element );
		this.uuid = widget_uuid++;
		this.eventNamespace = "." + this.widgetName + this.uuid;

		this.bindings = $();
		this.hoverable = $();
		this.focusable = $();

		if ( element !== this ) {
			$.data( element, this.widgetFullName, this );
			this._on( true, this.element, {
				remove: function( event ) {
					if ( event.target === element ) {
						this.destroy();
					}
				}
			});
			this.document = $( element.style ?
				// element within the document
				element.ownerDocument :
				// element is window or document
				element.document || element );
			this.window = $( this.document[0].defaultView || this.document[0].parentWindow );
		}

		this.options = $.widget.extend( {},
			this.options,
			this._getCreateOptions(),
			options );

		this._create();
		this._trigger( "create", null, this._getCreateEventData() );
		this._init();
	},
	_getCreateOptions: $.noop,
	_getCreateEventData: $.noop,
	_create: $.noop,
	_init: $.noop,

	destroy: function() {
		this._destroy();
		// we can probably remove the unbind calls in 2.0
		// all event bindings should go through this._on()
		this.element
			.unbind( this.eventNamespace )
			.removeData( this.widgetFullName )
			// support: jquery <1.6.3
			// http://bugs.jquery.com/ticket/9413
			.removeData( $.camelCase( this.widgetFullName ) );
		this.widget()
			.unbind( this.eventNamespace )
			.removeAttr( "aria-disabled" )
			.removeClass(
				this.widgetFullName + "-disabled " +
				"ui-state-disabled" );

		// clean up events and states
		this.bindings.unbind( this.eventNamespace );
		this.hoverable.removeClass( "ui-state-hover" );
		this.focusable.removeClass( "ui-state-focus" );
	},
	_destroy: $.noop,

	widget: function() {
		return this.element;
	},

	option: function( key, value ) {
		var options = key,
			parts,
			curOption,
			i;

		if ( arguments.length === 0 ) {
			// don't return a reference to the internal hash
			return $.widget.extend( {}, this.options );
		}

		if ( typeof key === "string" ) {
			// handle nested keys, e.g., "foo.bar" => { foo: { bar: ___ } }
			options = {};
			parts = key.split( "." );
			key = parts.shift();
			if ( parts.length ) {
				curOption = options[ key ] = $.widget.extend( {}, this.options[ key ] );
				for ( i = 0; i < parts.length - 1; i++ ) {
					curOption[ parts[ i ] ] = curOption[ parts[ i ] ] || {};
					curOption = curOption[ parts[ i ] ];
				}
				key = parts.pop();
				if ( arguments.length === 1 ) {
					return curOption[ key ] === undefined ? null : curOption[ key ];
				}
				curOption[ key ] = value;
			} else {
				if ( arguments.length === 1 ) {
					return this.options[ key ] === undefined ? null : this.options[ key ];
				}
				options[ key ] = value;
			}
		}

		this._setOptions( options );

		return this;
	},
	_setOptions: function( options ) {
		var key;

		for ( key in options ) {
			this._setOption( key, options[ key ] );
		}

		return this;
	},
	_setOption: function( key, value ) {
		this.options[ key ] = value;

		if ( key === "disabled" ) {
			this.widget()
				.toggleClass( this.widgetFullName + "-disabled", !!value );

			// If the widget is becoming disabled, then nothing is interactive
			if ( value ) {
				this.hoverable.removeClass( "ui-state-hover" );
				this.focusable.removeClass( "ui-state-focus" );
			}
		}

		return this;
	},

	enable: function() {
		return this._setOptions({ disabled: false });
	},
	disable: function() {
		return this._setOptions({ disabled: true });
	},

	_on: function( suppressDisabledCheck, element, handlers ) {
		var delegateElement,
			instance = this;

		// no suppressDisabledCheck flag, shuffle arguments
		if ( typeof suppressDisabledCheck !== "boolean" ) {
			handlers = element;
			element = suppressDisabledCheck;
			suppressDisabledCheck = false;
		}

		// no element argument, shuffle and use this.element
		if ( !handlers ) {
			handlers = element;
			element = this.element;
			delegateElement = this.widget();
		} else {
			element = delegateElement = $( element );
			this.bindings = this.bindings.add( element );
		}

		$.each( handlers, function( event, handler ) {
			function handlerProxy() {
				// allow widgets to customize the disabled handling
				// - disabled as an array instead of boolean
				// - disabled class as method for disabling individual parts
				if ( !suppressDisabledCheck &&
						( instance.options.disabled === true ||
							$( this ).hasClass( "ui-state-disabled" ) ) ) {
					return;
				}
				return ( typeof handler === "string" ? instance[ handler ] : handler )
					.apply( instance, arguments );
			}

			// copy the guid so direct unbinding works
			if ( typeof handler !== "string" ) {
				handlerProxy.guid = handler.guid =
					handler.guid || handlerProxy.guid || $.guid++;
			}

			var match = event.match( /^([\w:-]*)\s*(.*)$/ ),
				eventName = match[1] + instance.eventNamespace,
				selector = match[2];
			if ( selector ) {
				delegateElement.delegate( selector, eventName, handlerProxy );
			} else {
				element.bind( eventName, handlerProxy );
			}
		});
	},

	_off: function( element, eventName ) {
		eventName = (eventName || "").split( " " ).join( this.eventNamespace + " " ) +
			this.eventNamespace;
		element.unbind( eventName ).undelegate( eventName );

		// Clear the stack to avoid memory leaks (#10056)
		this.bindings = $( this.bindings.not( element ).get() );
		this.focusable = $( this.focusable.not( element ).get() );
		this.hoverable = $( this.hoverable.not( element ).get() );
	},

	_delay: function( handler, delay ) {
		function handlerProxy() {
			return ( typeof handler === "string" ? instance[ handler ] : handler )
				.apply( instance, arguments );
		}
		var instance = this;
		return setTimeout( handlerProxy, delay || 0 );
	},

	_hoverable: function( element ) {
		this.hoverable = this.hoverable.add( element );
		this._on( element, {
			mouseenter: function( event ) {
				$( event.currentTarget ).addClass( "ui-state-hover" );
			},
			mouseleave: function( event ) {
				$( event.currentTarget ).removeClass( "ui-state-hover" );
			}
		});
	},

	_focusable: function( element ) {
		this.focusable = this.focusable.add( element );
		this._on( element, {
			focusin: function( event ) {
				$( event.currentTarget ).addClass( "ui-state-focus" );
			},
			focusout: function( event ) {
				$( event.currentTarget ).removeClass( "ui-state-focus" );
			}
		});
	},

	_trigger: function( type, event, data ) {
		var prop, orig,
			callback = this.options[ type ];

		data = data || {};
		event = $.Event( event );
		event.type = ( type === this.widgetEventPrefix ?
			type :
			this.widgetEventPrefix + type ).toLowerCase();
		// the original event may come from any element
		// so we need to reset the target on the new event
		event.target = this.element[ 0 ];

		// copy original event properties over to the new event
		orig = event.originalEvent;
		if ( orig ) {
			for ( prop in orig ) {
				if ( !( prop in event ) ) {
					event[ prop ] = orig[ prop ];
				}
			}
		}

		this.element.trigger( event, data );
		return !( $.isFunction( callback ) &&
			callback.apply( this.element[0], [ event ].concat( data ) ) === false ||
			event.isDefaultPrevented() );
	}
};

$.each( { show: "fadeIn", hide: "fadeOut" }, function( method, defaultEffect ) {
	$.Widget.prototype[ "_" + method ] = function( element, options, callback ) {
		if ( typeof options === "string" ) {
			options = { effect: options };
		}
		var hasOptions,
			effectName = !options ?
				method :
				options === true || typeof options === "number" ?
					defaultEffect :
					options.effect || defaultEffect;
		options = options || {};
		if ( typeof options === "number" ) {
			options = { duration: options };
		}
		hasOptions = !$.isEmptyObject( options );
		options.complete = callback;
		if ( options.delay ) {
			element.delay( options.delay );
		}
		if ( hasOptions && $.effects && $.effects.effect[ effectName ] ) {
			element[ method ]( options );
		} else if ( effectName !== method && element[ effectName ] ) {
			element[ effectName ]( options.duration, options.easing, callback );
		} else {
			element.queue(function( next ) {
				$( this )[ method ]();
				if ( callback ) {
					callback.call( element[ 0 ] );
				}
				next();
			});
		}
	};
});

var widget = $.widget;



}));
/*
 * jQuery Easing v1.3 - http://gsgd.co.uk/sandbox/jquery/easing/
 *
 * Uses the built in easing capabilities added In jQuery 1.1
 * to offer multiple easing options
 *
 * TERMS OF USE - jQuery Easing
 * 
 * Open source under the BSD License. 
 * 
 * Copyright © 2008 George McGinley Smith
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, 
 * are permitted provided that the following conditions are met:
 * 
 * Redistributions of source code must retain the above copyright notice, this list of 
 * conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list 
 * of conditions and the following disclaimer in the documentation and/or other materials 
 * provided with the distribution.
 * 
 * Neither the name of the author nor the names of contributors may be used to endorse 
 * or promote products derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY 
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 *  COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 *  EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 *  GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED 
 * AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED 
 * OF THE POSSIBILITY OF SUCH DAMAGE. 
 *
*/

// t: current time, b: begInnIng value, c: change In value, d: duration
jQuery.easing['jswing'] = jQuery.easing['swing'];

jQuery.extend( jQuery.easing,
{
	def: 'easeOutQuad',
	swing: function (x, t, b, c, d) {
		//alert(jQuery.easing.default);
		return jQuery.easing[jQuery.easing.def](x, t, b, c, d);
	},
	easeInQuad: function (x, t, b, c, d) {
		return c*(t/=d)*t + b;
	},
	easeOutQuad: function (x, t, b, c, d) {
		return -c *(t/=d)*(t-2) + b;
	},
	easeInOutQuad: function (x, t, b, c, d) {
		if ((t/=d/2) < 1) return c/2*t*t + b;
		return -c/2 * ((--t)*(t-2) - 1) + b;
	},
	easeInCubic: function (x, t, b, c, d) {
		return c*(t/=d)*t*t + b;
	},
	easeOutCubic: function (x, t, b, c, d) {
		return c*((t=t/d-1)*t*t + 1) + b;
	},
	easeInOutCubic: function (x, t, b, c, d) {
		if ((t/=d/2) < 1) return c/2*t*t*t + b;
		return c/2*((t-=2)*t*t + 2) + b;
	},
	easeInQuart: function (x, t, b, c, d) {
		return c*(t/=d)*t*t*t + b;
	},
	easeOutQuart: function (x, t, b, c, d) {
		return -c * ((t=t/d-1)*t*t*t - 1) + b;
	},
	easeInOutQuart: function (x, t, b, c, d) {
		if ((t/=d/2) < 1) return c/2*t*t*t*t + b;
		return -c/2 * ((t-=2)*t*t*t - 2) + b;
	},
	easeInQuint: function (x, t, b, c, d) {
		return c*(t/=d)*t*t*t*t + b;
	},
	easeOutQuint: function (x, t, b, c, d) {
		return c*((t=t/d-1)*t*t*t*t + 1) + b;
	},
	easeInOutQuint: function (x, t, b, c, d) {
		if ((t/=d/2) < 1) return c/2*t*t*t*t*t + b;
		return c/2*((t-=2)*t*t*t*t + 2) + b;
	},
	easeInSine: function (x, t, b, c, d) {
		return -c * Math.cos(t/d * (Math.PI/2)) + c + b;
	},
	easeOutSine: function (x, t, b, c, d) {
		return c * Math.sin(t/d * (Math.PI/2)) + b;
	},
	easeInOutSine: function (x, t, b, c, d) {
		return -c/2 * (Math.cos(Math.PI*t/d) - 1) + b;
	},
	easeInExpo: function (x, t, b, c, d) {
		return (t==0) ? b : c * Math.pow(2, 10 * (t/d - 1)) + b;
	},
	easeOutExpo: function (x, t, b, c, d) {
		return (t==d) ? b+c : c * (-Math.pow(2, -10 * t/d) + 1) + b;
	},
	easeInOutExpo: function (x, t, b, c, d) {
		if (t==0) return b;
		if (t==d) return b+c;
		if ((t/=d/2) < 1) return c/2 * Math.pow(2, 10 * (t - 1)) + b;
		return c/2 * (-Math.pow(2, -10 * --t) + 2) + b;
	},
	easeInCirc: function (x, t, b, c, d) {
		return -c * (Math.sqrt(1 - (t/=d)*t) - 1) + b;
	},
	easeOutCirc: function (x, t, b, c, d) {
		return c * Math.sqrt(1 - (t=t/d-1)*t) + b;
	},
	easeInOutCirc: function (x, t, b, c, d) {
		if ((t/=d/2) < 1) return -c/2 * (Math.sqrt(1 - t*t) - 1) + b;
		return c/2 * (Math.sqrt(1 - (t-=2)*t) + 1) + b;
	},
	easeInElastic: function (x, t, b, c, d) {
		var s=1.70158;var p=0;var a=c;
		if (t==0) return b;  if ((t/=d)==1) return b+c;  if (!p) p=d*.3;
		if (a < Math.abs(c)) { a=c; var s=p/4; }
		else var s = p/(2*Math.PI) * Math.asin (c/a);
		return -(a*Math.pow(2,10*(t-=1)) * Math.sin( (t*d-s)*(2*Math.PI)/p )) + b;
	},
	easeOutElastic: function (x, t, b, c, d) {
		var s=1.70158;var p=0;var a=c;
		if (t==0) return b;  if ((t/=d)==1) return b+c;  if (!p) p=d*.3;
		if (a < Math.abs(c)) { a=c; var s=p/4; }
		else var s = p/(2*Math.PI) * Math.asin (c/a);
		return a*Math.pow(2,-10*t) * Math.sin( (t*d-s)*(2*Math.PI)/p ) + c + b;
	},
	easeInOutElastic: function (x, t, b, c, d) {
		var s=1.70158;var p=0;var a=c;
		if (t==0) return b;  if ((t/=d/2)==2) return b+c;  if (!p) p=d*(.3*1.5);
		if (a < Math.abs(c)) { a=c; var s=p/4; }
		else var s = p/(2*Math.PI) * Math.asin (c/a);
		if (t < 1) return -.5*(a*Math.pow(2,10*(t-=1)) * Math.sin( (t*d-s)*(2*Math.PI)/p )) + b;
		return a*Math.pow(2,-10*(t-=1)) * Math.sin( (t*d-s)*(2*Math.PI)/p )*.5 + c + b;
	},
	easeInBack: function (x, t, b, c, d, s) {
		if (s == undefined) s = 1.70158;
		return c*(t/=d)*t*((s+1)*t - s) + b;
	},
	easeOutBack: function (x, t, b, c, d, s) {
		if (s == undefined) s = 1.70158;
		return c*((t=t/d-1)*t*((s+1)*t + s) + 1) + b;
	},
	easeInOutBack: function (x, t, b, c, d, s) {
		if (s == undefined) s = 1.70158; 
		if ((t/=d/2) < 1) return c/2*(t*t*(((s*=(1.525))+1)*t - s)) + b;
		return c/2*((t-=2)*t*(((s*=(1.525))+1)*t + s) + 2) + b;
	},
	easeInBounce: function (x, t, b, c, d) {
		return c - jQuery.easing.easeOutBounce (x, d-t, 0, c, d) + b;
	},
	easeOutBounce: function (x, t, b, c, d) {
		if ((t/=d) < (1/2.75)) {
			return c*(7.5625*t*t) + b;
		} else if (t < (2/2.75)) {
			return c*(7.5625*(t-=(1.5/2.75))*t + .75) + b;
		} else if (t < (2.5/2.75)) {
			return c*(7.5625*(t-=(2.25/2.75))*t + .9375) + b;
		} else {
			return c*(7.5625*(t-=(2.625/2.75))*t + .984375) + b;
		}
	},
	easeInOutBounce: function (x, t, b, c, d) {
		if (t < d/2) return jQuery.easing.easeInBounce (x, t*2, 0, c, d) * .5 + b;
		return jQuery.easing.easeOutBounce (x, t*2-d, 0, c, d) * .5 + c*.5 + b;
	}
});

/*
 *
 * TERMS OF USE - EASING EQUATIONS
 * 
 * Open source under the BSD License. 
 * 
 * Copyright © 2001 Robert Penner
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, 
 * are permitted provided that the following conditions are met:
 * 
 * Redistributions of source code must retain the above copyright notice, this list of 
 * conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list 
 * of conditions and the following disclaimer in the documentation and/or other materials 
 * provided with the distribution.
 * 
 * Neither the name of the author nor the names of contributors may be used to endorse 
 * or promote products derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY 
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 *  COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 *  EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 *  GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED 
 * AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED 
 * OF THE POSSIBILITY OF SUCH DAMAGE. 
 *
 */
/*! Copyright (c) 2013 Brandon Aaron (http://brandonaaron.net)
 * Licensed under the MIT License (LICENSE.txt).
 *
 * Thanks to: http://adomas.org/javascript-mouse-wheel/ for some pointers.
 * Thanks to: Mathias Bank(http://www.mathias-bank.de) for a scope bug fix.
 * Thanks to: Seamus Leahy for adding deltaX and deltaY
 *
 * Version: 3.1.3
 *
 * Requires: 1.2.2+
 */

(function (factory) {
    if ( typeof define === 'function' && define.amd ) {
        // AMD. Register as an anonymous module.
        define(['jquery'], factory);
    } else if (typeof exports === 'object') {
        // Node/CommonJS style for Browserify
        module.exports = factory;
    } else {
        // Browser globals
        factory(jQuery);
    }
}(function ($) {

    var toFix = ['wheel', 'mousewheel', 'DOMMouseScroll', 'MozMousePixelScroll'];
    var toBind = 'onwheel' in document || document.documentMode >= 9 ? ['wheel'] : ['mousewheel', 'DomMouseScroll', 'MozMousePixelScroll'];
    var lowestDelta, lowestDeltaXY;

    if ( $.event.fixHooks ) {
        for ( var i = toFix.length; i; ) {
            $.event.fixHooks[ toFix[--i] ] = $.event.mouseHooks;
        }
    }

    $.event.special.mousewheel = {
        setup: function() {
            if ( this.addEventListener ) {
                for ( var i = toBind.length; i; ) {
                    this.addEventListener( toBind[--i], handler, false );
                }
            } else {
                this.onmousewheel = handler;
            }
        },

        teardown: function() {
            if ( this.removeEventListener ) {
                for ( var i = toBind.length; i; ) {
                    this.removeEventListener( toBind[--i], handler, false );
                }
            } else {
                this.onmousewheel = null;
            }
        }
    };

    $.fn.extend({
        mousewheel: function(fn) {
            return fn ? this.bind("mousewheel", fn) : this.trigger("mousewheel");
        },

        unmousewheel: function(fn) {
            return this.unbind("mousewheel", fn);
        }
    });


    function handler(event) {
        var orgEvent = event || window.event,
            args = [].slice.call(arguments, 1),
            delta = 0,
            deltaX = 0,
            deltaY = 0,
            absDelta = 0,
            absDeltaXY = 0,
            fn;
        event = $.event.fix(orgEvent);
        event.type = "mousewheel";

        // Old school scrollwheel delta
        if ( orgEvent.wheelDelta ) { delta = orgEvent.wheelDelta; }
        if ( orgEvent.detail )     { delta = orgEvent.detail * -1; }

        // New school wheel delta (wheel event)
        if ( orgEvent.deltaY ) {
            deltaY = orgEvent.deltaY * -1;
            delta  = deltaY;
        }
        if ( orgEvent.deltaX ) {
            deltaX = orgEvent.deltaX;
            delta  = deltaX * -1;
        }

        // Webkit
        if ( orgEvent.wheelDeltaY !== undefined ) { deltaY = orgEvent.wheelDeltaY; }
        if ( orgEvent.wheelDeltaX !== undefined ) { deltaX = orgEvent.wheelDeltaX * -1; }

        // Look for lowest delta to normalize the delta values
        absDelta = Math.abs(delta);
        if ( !lowestDelta || absDelta < lowestDelta ) { lowestDelta = absDelta; }
        absDeltaXY = Math.max(Math.abs(deltaY), Math.abs(deltaX));
        if ( !lowestDeltaXY || absDeltaXY < lowestDeltaXY ) { lowestDeltaXY = absDeltaXY; }

        // Get a whole value for the deltas
        fn = delta > 0 ? 'floor' : 'ceil';
        delta  = Math[fn](delta / lowestDelta);
        deltaX = Math[fn](deltaX / lowestDeltaXY);
        deltaY = Math[fn](deltaY / lowestDeltaXY);

        // Add event and delta to the front of the arguments
        args.unshift(event, delta, deltaX, deltaY);

        return ($.event.dispatch || $.event.handle).apply(this, args);
    }

}));

/**
* Copyright (c) 2014, Leon Sorokin
* All rights reserved. (MIT Licensed)
*
* preCode.js - painkiller for <pre><code> & <textarea>
*/

(function() {
	function preCode(selector) {
		var els = Array.prototype.slice.call(document.querySelectorAll(selector), 0);

		els.forEach(function(el, idx, arr){
			var txt = el.textContent
				.replace(/^[\r\n]+/, "")	// strip leading newline
				.replace(/\s+$/g, "");

			if (/^\S/gm.test(txt)) {
				el.textContent = txt;
				return;
			}

			var mat, str, re = /^[\t ]+/gm, len, min = 1e3;

			while (mat = re.exec(txt)) {
				len = mat[0].length;

				if (len < min) {
					min = len;
					str = mat[0];
				}
			}

			if (min == 1e3)
				return;

			el.textContent = txt.replace(new RegExp("^" + str, 'gm'), "");
		});
	}

	document.addEventListener("DOMContentLoaded", function() {
		preCode("pre code, textarea");
	}, false);
})();
var hasTouch = 'ontouchend' in window, eventTimer;
var moveDirection = 'undefined', startX, startY, deltaX, deltaY, mouseDown = false;

function addTouchEvents(element){
    if (hasTouch) {
        element.addEventListener("touchstart", touch2Mouse, true);
        element.addEventListener("touchmove", touch2Mouse, true);
        element.addEventListener("touchend", touch2Mouse, true);
    }
}

function touch2Mouse(e)
{
    var theTouch = e.changedTouches[0];
    var mouseEv;

    switch(e.type)
    {
        case "touchstart": mouseEv="mousedown"; break;
        case "touchend":   mouseEv="mouseup"; break;
        case "touchmove":  mouseEv="mousemove"; break;
        default: return;
    }


    if (mouseEv == "mousedown") {
        eventTimer = (new Date()).getTime();
        startX = theTouch.clientX;
        startY = theTouch.clientY;
        mouseDown = true;
    }

    if (mouseEv == "mouseup") {
        if ((new Date()).getTime() - eventTimer <= 500) {
            mouseEv = "click";
        } else if ((new Date()).getTime() - eventTimer > 1000) {
            mouseEv = "longclick";
        }
        eventTimer = 0;
        mouseDown = false;
    }

    if (mouseEv == "mousemove") {
        if (mouseDown) {
            deltaX = theTouch.clientX - startX;
            deltaY = theTouch.clientY - startY;
            moveDirection = deltaX > deltaY ? 'horizontal' : 'vertical';
        }
    }

    var mouseEvent = document.createEvent("MouseEvent");
    mouseEvent.initMouseEvent(mouseEv, true, true, window, 1, theTouch.screenX, theTouch.screenY, theTouch.clientX, theTouch.clientY, false, false, false, false, 0, null);
    theTouch.target.dispatchEvent(mouseEvent);

    e.preventDefault();
}

(function( $ ) {
    "use strict";

    $.widget("metro.accordion", {

        version: "3.0.0",

        options: {
            closeAny: false,
            speed: 'fast',
            onFrameOpen: function(frame){return true;},
            onFrameOpened: function(frame){},
            onFrameClose: function(frame){return true;},
            onFrameClosed: function(frame){}
        },

        init: function(){
            var that = this, element = this.element;

            element.on('click', '.heading', function(e){
                var frame = $(this).parent();

                if (frame.hasClass('disabled')) {return false;}

                if  (!frame.hasClass('active')) {
                    that._openFrame(frame);
                } else {
                    that._closeFrame(frame);
                }

                e.preventDefault();
                e.stopPropagation();
            });
        },

        _closeAllFrames: function(){
            var that = this;
            var frames = this.element.children('.frame.active');
            $.each(frames, function(){
                that._closeFrame($(this));
            });
        },

        _openFrame: function(frame){
            var o = this.options;
            var content = frame.children('.content');

            if (typeof o.onFrameOpen === 'string') {
                if (!window[o.onFrameOpen](frame)) {return false;}
            } else {
                if (!o.onFrameOpen(frame)) {return false;}
            }

            if (o.closeAny) {this._closeAllFrames();}

            content.slideDown(o.speed);
            frame.addClass('active');

            if (typeof o.onFrameOpened === 'string') {
                window[o.onFrameOpened](frame);
            } else {
                o.onFrameOpened(frame);
            }
        },

        _closeFrame: function(frame){
            var o = this.options;
            var content = frame.children('.content');

            if (typeof o.onFrameClose === 'string') {
                if (!window[o.onFrameClose](frame)) {return false;}
            } else {
                if (!o.onFrameClose(frame)) {return false;}
            }

            content.slideUp(o.speed,function(){
                frame.removeClass("active");
            });

            if (typeof o.onFrameClosed === 'string') {
                window[o.onFrameClosed](frame);
            } else {
                o.onFrameClosed(frame);
            }
        },

        _create: function(){
            var that = this, o = this.options, element = this.element;

            this._setOptionsData();

            that.init();
            element.data('accordion', this);

        },

        _setOptionsData: function(){
            var o = this.options;

            $.each(this.element.data(), function(key, value){
                if (key in o) {
                    try {
                        o[key] = $.parseJSON(value);
                    } catch (e) {
                        o[key] = value;
                    }
                }
            });
        },

        _destroy: function(){
        },

        _setOption: function(key, value){
            this._super('_setOption', key, value);
        }
    });
})( jQuery );
(function ( $ ) {

    "use strict";

    $.widget( "metro.appbar" , {

        version: "3.0.0",

        options: {
        },

        _create: function () {
            var that = this, element = this.element, o = this.options;

            $.each(element.data(), function(key, value){
                if (key in o) {
                    try {
                        o[key] = $.parseJSON(value);
                    } catch (e) {
                        o[key] = value;
                    }
                }
            });

            this._initBar();

            element.data('appbar', this);

        },

        _initBar: function(){
            var that = this, element = this.element, o = this.options;
            var pull = $(element).find('.app-bar-pull');
            var menu = $(element).find('.app-bar-menu');

            if (menu.length === 0) {
                pull.hide();
            }

            if (pull.length > 0) {
                pull.on('click', function(e){
                    menu.slideToggle('fast');
                    e.preventDefault();
                    e.stopPropagation();
                });
            }

            if (menu.length > 0) {
                $(window).resize(function(){
                    var device_width = (window.innerWidth > 0) ? window.innerWidth : screen.width;
                    if (device_width > 800) {
                        $(".app-bar:not(.no-responsive-future) .app-bar-menu").show();
                    } else {
                        $(".app-bar:not(.no-responsive-future) .app-bar-menu").hide();
                    }
                });
            }
        },

        _destroy: function () {
        },

        _setOption: function ( key, value ) {
            this._super('_setOption', key, value);
        }
    });

})( jQuery );
(function ( $ ) {
    "use strict";

    $.widget( "metro.buttonGroup" , {

        version: "3.0.0",

        options: {
            groupType: 'one-state', // 'multi-state'
            buttonStyle: false,
            onChange: function(index, btn){return true;},
            onChanged: function(index, btn){}
        },

        _create: function () {
            var that = this, element = this.element, o = this.options;

            $.each(element.data(), function(key, value){
                if (key in o) {
                    try {
                        o[key] = $.parseJSON(value);
                    } catch (e) {
                        o[key] = value;
                    }
                }
            });

            if (!element.hasClass('group-of-buttons')) {element.addClass('group-of-buttons');}

            var buttons = element.find('.button, .toolbar-button');

            for(var i = 0; i < buttons.length; i++) {
                $(buttons[i]).data('index', i);
            }

            if (o.buttonStyle !== false) {
                buttons.addClass(o.buttonStyle);
            }

            element.on('click', '.button, .toolbar-button', function(){

                if (typeof o.onChange === 'string') {
                    if (!window[o.onChange]($(this).data('index'), this)) {return false;}
                } else {
                    if (!o.onChange($(this).data('index'), this)) {return false;}
                }

                if (o.groupType === 'one-state') {
                    buttons.removeClass('active');
                    $(this).addClass('active');
                } else  {
                    $(this).toggleClass('active');
                }

                if (typeof o.onChanged === 'string') {
                    window[o.onChanged]($(this).data('index'), this);
                } else {
                    o.onChanged($(this).data('index'), this);
                }
            });

            element.data('buttonGroup', this);

        },

        _destroy: function () {
        },

        _setOption: function ( key, value ) {
            this._super('_setOption', key, value);
        }
    });

})( jQuery );
window.METRO_CALENDAR_WEEK_START = 0;
window.METRO_LOCALES = {
    'en': {
        months: [
            "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December",
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
        ],
        days: [
            "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday",
            "Su", "Mo", "Tu", "We", "Th", "Fr", "Sa"
        ],
        buttons: [
            "Today", "Clear", "Cancel", "Help", "Prior", "Next", "Finish"
        ]
    },
    'fr': {
        months: [
            "Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre",
            "Jan", "Fév", "Mars", "Avr", "Mai", "Juin", "Juil", "Août", "Sept", "Oct", "Nov", "Déc"
        ],
        days: [
            "Dimanche", "Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi",
            "Di", "Lu", "Ma", "Me", "Je", "Ve", "Sa"
        ],
        buttons: [
            "Aujourd'hui", "Effacer", "Annuler", "Aide", "Précedent", "Suivant", "Fin"
        ]
    },
    'nl': {
        months: [
            "Januari", "Februari", "Maart", "April", "Mei", "Juni", "Juli", "Augustus", "September", "Oktober", "November", "December",
            "Jan", "Feb", "Mrt", "Apr", "Mei", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec"
        ],
        days: [
            "Zondag", "Maandag", "Dinsdag", "Woensdag", "Donderdag", "Vrijdag", "Zaterdag",
            "Zo", "Ma", "Di", "Wo", "Do", "Vr", "Za"
        ],
        buttons: [
            "Vandaag", "Verwijderen", "Annuleren", "Hulp", "Vorige", "Volgende", "Einde"
        ]
    },
    'ua': {
        months: [
            "Січень", "Лютий", "Березень", "Квітень", "Травень", "Червень", "Липень", "Серпень", "Вересень", "Жовтень", "Листопад", "Грудень",
            "Січ", "Лют", "Бер", "Кві", "Тра", "Чер", "Лип", "Сер", "Вер", "Жов", "Лис", "Гру"
        ],
        days: [
            "Неділя", "Понеділок", "Вівторок", "Середа", "Четвер", "П’ятниця", "Субота",
            "Нд", "Пн", "Вт", "Ср", "Чт", "Пт", "Сб"
        ],
        buttons: [
            "Сьогодні", "Очистити", "Скасувати", "Допомога", "Назад", "Вперед", "Готово"
        ]
    },
    'ru': {
        months: [
            "Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь",
            "Янв", "Фев", "Мар", "Апр", "Май", "Июн", "Июл", "Авг", "Сен", "Окт", "Ноя", "Дек"
        ],
        days: [
            "Воскресенье", "Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота",
            "Вс", "Пн", "Вт", "Ср", "Чт", "Пт", "Сб"
        ],
        buttons: [
            "Сегодня", "Очистить", "Отменить", "Помощь", "Назад", "Вперед", "Готово"
        ]
    },
    /** By NoGrief (nogrief@gmail.com) */
    'zhCN': {
        months: [
            "一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月",
            "一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"
        ],
        days: [
            "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六",
            "日", "一", "二", "三", "四", "五", "六"
        ],
        buttons: [
            "今日", "清除", "Cancel", "Help", "Prior", "Next", "Finish"
        ]
    },
    'it': {
        months: [
            'Gennaio', 'Febbraio', 'Marzo', 'Aprile', 'Maggio', 'Giugno', 'Luglio', 'Agosto', 'Settembre', 'Ottobre', 'Novembre', 'Dicembre',
            'Gen',' Feb', 'Mar', 'Apr', 'Mag', 'Giu', 'Lug', 'Ago', 'Set', 'Ott', 'Nov', 'Dic'
        ],
        days: [
            'Lunedì', 'Martedì', 'Mercoledì', 'Giovedì', 'Venerdì', 'Sabato', 'Domenica',
            'Lun', 'Mar', 'Mer', 'Gio', 'Ven', 'Sab', 'Dom'
        ],
        buttons: [
            "Oggi", "Cancella", "Cancel", "Help", "Prior", "Next", "Finish"
        ]
    },
    'de': {
        months: [
            "Januar", "Februar", "März", "April", "Mai", "Juni", "Juli", "August", "September", "Oktober", "November", "Dezember",
            "Jan", "Feb", "Mrz", "Apr", "Mai", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dez"
        ],
        days: [
            "Sonntag", "Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag",
            "So", "Mo", "Di", "Mi", "Do", "Fr", "Sa"
        ],
        buttons: [
            "Heute", "Zurücksetzen", "Abbrechen", "Hilfe", "Früher", "Später", "Fertig"
        ]
    },
    /** By Javier Rodríguez (javier.rodriguez at fjrodriguez.com) */
    'es': {
        months: [
            "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre",
            "Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sept", "Oct", "Nov", "Dic"
        ],
        days: [
            "Domingo", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado",
            "Do", "Lu", "Mar", "Mié", "Jue", "Vi", "Sáb"
        ],
        buttons: [
            "Hoy", "Limpiar", "Cancel", "Help", "Prior", "Next", "Finish"
        ]
    },
    'pt': {
        months: [
            'Janeiro','Fevereiro','Março','Abril','Maio','Junho','Julho','Agosto','Setembro','Outubro','Novembro','Dezembro',
            'Jan','Fev','Mar','Abr','Mai','Jun','Jul','Ago','Set','Out','Nov','Dez'
        ],
        days: [
            'Domingo','Segunda-feira','Terça-feira','Quarta-feira','Quinta-feira','Sexta-feira','Sabado',
            'Dom','Seg','Ter','Qua','Qui','Sex','Sab'
        ],
        buttons: [
            "Hoje", "Limpar", "Cancelar", "Ajuda", "Anterior", "Seguinte", "Terminar"
        ]
    },
    'pl': {
        months: [
            "Styczeń", "Luty", "Marzec", "Kwiecień", "Maj", "Czerwiec", "Lipiec", "Sierpień", "Wrzesień", "Październik", "Listopad", "Grudzień",
            "Sty", "Lut", "Mar", "Kwi", "Maj", "Cze", "Lip", "Sie", "Wrz", "Paź", "Lis", "Gru"
        ],
        days: [
            "Niedziela", "Poniedziałek", "Wtorek", "Środa", "Czwartek", "Piątek", "Sobota",
            "Nd", "Pon", "Wt", "Śr", "Czw", "Pt", "Sob"
        ],
        buttons: [
            "Dzisiaj", "Wyczyść", "Anuluj", "Pomoc", "Poprzedni", "Następny", "Koniec"
        ]
    },
    'cs': {
        months: [
            "Leden", "Únor", "Březen", "Duben", "Květen", "Červen", "Červenec", "Srpen", "Září", "Říjen", "Listopad", "Prosinec",
            "Led", "Ún", "Bř", "Dub", "Kvě", "Če", "Čer", "Srp", "Zá", "Ří", "Li", "Pro"
        ],
        days: [
            "Neděle", "Pondělí", "Úterý", "Středa", "Čtvrtek", "Pátek", "Sobota",
            "Ne", "Po", "Út", "St", "Čt", "Pá", "So"
        ],
        buttons: [
            "Dnes", "Vyčistit", "Zrušit", "Pomoc", "Předešlý", "Další", "Dokončit"
        ]
    }
};

(function( $ ) {
    "use strict";

    $.widget("metro.calendar", {

        version: "3.0.0",

        options: {
            format: "yyyy-mm-dd",
            multiSelect: false,
            startMode: 'day', //year, month, day
            weekStart: window.METRO_CALENDAR_WEEK_START, // 0 - Sunday, 1 - Monday
            otherDays: true,
            date: new Date(),
            minDate: false,
            preset: false,
            exclude: false,
            buttons: true,
            buttonToday: true,
            buttonClear: true,
            locale: 'en',
            actions: true,
            condensedGrid: false,
            getDates: function(d){},
            dayClick: function(d, d0){}
        },

        //_storage: [],
        //_exclude: [],

        _year: 0,
        _month: 0,
        _day: 0,
        _today: new Date(),
        _event: '',

        _mode: 'day', // day, month, year
        _distance: 0,

        _events: [],

        _create: function(){
            var that = this, element = this.element, o = this.options;

            $.each(element.data(), function(key, value){
                if (key in o) {
                    try {
                        o[key] = $.parseJSON(value);
                    } catch (e) {
                        o[key] = value;
                    }
                }
            });

            if (typeof  o.date === 'string') {
                o.date = new Date(o.date);
            }

            if (o.minDate !== false && typeof  o.minDate === 'string') {
                o.minDate = new Date(o.minDate);
            }

            this.locales = window.METRO_LOCALES;

            //console.log(o.date);

            this._year = o.date.getFullYear();
            this._distance = o.date.getFullYear()-4;
            this._month = o.date.getMonth();
            this._day = o.date.getDate();
            this._mode = o.startMode;

            element.data("_storage", []);
            element.data("_exclude", []);
            if (!element.hasClass('calendar')) {element.addClass('calendar');}

            var re, dates;

            if (o.preset) {
                re = /\s*,\s*/;
                dates = o.preset.split(re);
                $.each(dates, function(){
                    if (new Date(this) !== undefined) {that.setDate(this);}
                });
            }

            if (o.exclude) {
                re = /\s*,\s*/;
                dates = o.exclude.split(re);
                $.each(dates, function(){
                    if (new Date(this) !== undefined) {that.setDateExclude(this);}
                });
            }

            this._renderCalendar();

            element.data('calendar', this);

        },

        _renderButtons: function(table){
            var tr, td, o = this.options;

            if (this.options.buttons) {

                var buttonToday = o.buttonToday ? "<button class='button calendar-btn-today small-button success'>"+this.locales[o.locale].buttons[0]+"</button>" : "";
                var buttonClear = o.buttonClear ? "<button class='button calendar-btn-clear small-button warning'>"+this.locales[o.locale].buttons[1]+"</button>" : "";

                tr = $("<div/>").addClass("calendar-row calendar-actions");
                td = $("<div/>").addClass("align-center").html(
                    buttonToday + buttonClear
                );
                td.appendTo(tr);
                tr.appendTo(table);
            }
        },

        _renderMonth: function(){
            var that = this, o = this.options,
                year = this._year,
                month = this._month,
                day = this._day,
                event = this._event,
                feb = 28;

            if (month === 1) {
                if ((year%100 !== 0) && (year%4 === 0) || (year%400 === 0)) {
                    feb = 29;
                }
            }

            var totalDays = ["31", ""+feb+"","31","30","31","30","31","31","30","31","30","31"];
            var daysInMonth = totalDays[month];
            var first_week_day = new Date(year, month, 1).getDay();

            var table, tr, td, i, div;

            this.element.html("");

            table = $("<div/>").addClass("calendar-grid");
            if (o.condensedGrid) {
                table.addClass('condensed no-border');
            }

            //console.log(this.locales);

            // Add calendar header
            tr = $("<div/>").addClass('calendar-row no-margin');

            $("<div/>").addClass("calendar-cell align-center").html("<a class='btn-previous-year' href='#'>-</a>").appendTo(tr);
            $("<div/>").addClass("calendar-cell align-center").html("<a class='btn-previous-month' href='#'>&#12296;</a>").appendTo(tr);

            $("<div/>").addClass("calendar-cell sel-month align-center").html("<a class='btn-select-month' href='#'>"+ this.locales[o.locale].months[month]+' '+year+"</a>").appendTo(tr);

            $("<div/>").addClass("calendar-cell align-center").html("<a class='btn-next-month' href='#'>&#12297;</a>").appendTo(tr);
            $("<div/>").addClass("calendar-cell align-center").html("<a class='btn-next-year' href='#'>+</a>").appendTo(tr);

            tr.addClass("calendar-header").appendTo(table);

            // Add day names
            var j;
            tr = $("<div/>").addClass('calendar-row week-days');
            for(i = 0; i < 7; i++) {
                if (!o.weekStart) {
                    td = $("<div/>").addClass("calendar-cell align-center day-of-week").appendTo(tr);
                    div = $("<div/>").html(this.locales[o.locale].days[i + 7]).appendTo(td);
                } else {
                    j = i + 1;
                    if (j === 7) {j = 0;}
                    td = $("<div/>").addClass("calendar-cell align-center day-of-week").appendTo(tr);
                    div = $("<div/>").html(this.locales[o.locale].days[j+7]).appendTo(td);
                }
            }
            tr.addClass("calendar-subheader").appendTo(table);

            // Add empty days for previos month
            var prevMonth = this._month - 1; if (prevMonth < 0) {prevMonth = 11;} var daysInPrevMonth = totalDays[prevMonth];
            var _first_week_day = ((o.weekStart) ? first_week_day + 6 : first_week_day)%7;
            var htmlPrevDay = "";
            tr = $("<div/>").addClass('calendar-row');
            for(i = 0; i < _first_week_day; i++) {
                if (o.otherDays) {htmlPrevDay = daysInPrevMonth - (_first_week_day - i - 1);}
                td = $("<div/>").addClass("calendar-cell empty").appendTo(tr);
                div = $("<div/>").addClass('other-day').html(htmlPrevDay).appendTo(td);
                if (!o.otherDays) {
                    div.css('visibility', 'hidden');
                }
            }

            // Days for current month
            var week_day = ((o.weekStart) ? first_week_day + 6 : first_week_day)%7;

            var d, a, d_html;

            for (i = 1; i <= daysInMonth; i++) {
                week_day %= 7;

                if (week_day === 0) {
                    tr.appendTo(table);
                    tr = $("<div/>").addClass('calendar-row');
                }

                td = $("<div/>").addClass("calendar-cell align-center day");
                div = $("<div/>").appendTo(td);
                if (o.minDate !== false && (new Date(year, month, i) < o.minDate)) {
                    td.removeClass("day");
                    div.addClass("other-day");
                    d_html = i;
                } else {
                    d_html = "<a href='#'>"+i+"</a>";
                }

                div.html(d_html);

                //console.log(div);

                if (year === this._today.getFullYear() && month === this._today.getMonth() && this._today.getDate() === i) {
                    td.addClass("today");
                }

                //console.log('xxx');
                d = (new Date(this._year, this._month, i)).format('yyyy-mm-dd');

                if (this.element.data('_storage').indexOf(d)>=0) {
                    a = td.find("a");
                    a.parent().parent().addClass("selected");
                }

                if (this.element.data('_exclude').indexOf(d)>=0) {
                    a = td.find("a");
                    a.parent().parent().addClass("exclude");
                }

                td.appendTo(tr);
                week_day++;
            }


            // next month other days
            var htmlOtherDays = "";
            for (i = week_day+1; i<=7; i++){
                if (o.otherDays) {htmlOtherDays = i - week_day;}
                td = $("<div/>").addClass("calendar-cell empty").appendTo(tr);
                div = $("<div/>").addClass('other-day').html(htmlOtherDays).appendTo(td);
                if (!o.otherDays) {
                    div.css('visibility', 'hidden');
                }
            }

            tr.appendTo(table);

            this._renderButtons(table);

            table.appendTo(this.element);

            //if (typeof  o.getDates == 'string') {
            //    window[o.getDates](this.element.data('_storage'));
            //} else {
            //    o.getDates(this.element.data('_storage'));
            //}

        },

        _renderMonths: function(){
            var table, tr, td, i, j;

            this.element.html("");

            table = $("<div/>").addClass("calendar-grid");
            if (this.options.condensedGrid) {
                table.addClass('condensed no-border');
            }

            // Add calendar header
            tr = $("<div/>").addClass('calendar-row');

            $("<div/>").addClass("calendar-cell sel-minus align-center").html("<a class='btn-previous-year' href='#'>-</a>").appendTo(tr);
            $("<div/>").addClass("calendar-cell sel-year align-center").html("<a class='btn-select-year' href='#'>"+this._year+"</a>").appendTo(tr);
            $("<div/>").addClass("calendar-cell sel-plus align-center").html("<a class='btn-next-year' href='#'>+</a>").appendTo(tr);

            tr.addClass("calendar-header").appendTo(table);

            tr = $("<div/>").addClass('calendar-row');
            j = 0;
            for (i=0;i<12;i++) {

                //td = $("<td/>").addClass("text-center month").html("<a href='#' data-month='"+i+"'>"+this.options.monthsShort[i]+"</a>");
                td = $("<div/>").addClass("calendar-cell month-cell align-center month").html("<a href='#' data-month='"+i+"'>"+this.locales[this.options.locale].months[i+12]+"</a>");

                if (this._month === i && (new Date()).getFullYear() === this._year) {
                    td.addClass("today");
                }

                td.appendTo(tr);
                if ((j+1) % 4 === 0) {
                    tr.appendTo(table);
                    tr = $("<div/>").addClass('calendar-row');
                }
                j+=1;
            }

            this._renderButtons(table);

            table.appendTo(this.element);
        },

        _renderYears: function(){
            var table, tr, td, i, j;

            this.element.html("");

            table = $("<div/>").addClass("calendar-grid");
            if (this.options.condensedGrid) {
                table.addClass('condensed no-border');
            }

            // Add calendar header
            tr = $("<div/>").addClass('calendar-row cells4');

            $("<div/>").addClass("calendar-cell sel-minus align-center").html("<a class='btn-previous-year' href='#'>-</a>").appendTo(tr);
            $("<div/>").addClass("calendar-cell sel-year align-center").html("<a class='btn-none-btn'>" + (this._distance)+"-"+(this._distance+11) + "</a>").appendTo(tr);
            $("<div/>").addClass("calendar-cell sel-plus align-center").html("<a class='btn-next-year' href='#'>+</a>").appendTo(tr);

            tr.addClass("calendar-header").appendTo(table);

            tr = $("<div/>").addClass('calendar-row');

            j = 0;
            for (i=this._distance;i<this._distance+12;i++) {
                td = $("<div/>").addClass("calendar-cell year-cell align-center year").html("<a href='#' data-year='"+i+"'>"+i+"</a>");
                if ((new Date()).getFullYear() === i) {
                    td.addClass("today");
                }
                td.appendTo(tr);
                if ((j+1) % 4 === 0) {
                    tr.appendTo(table);
                    tr = $("<div/>").addClass('calendar-row');
                }
                j+=1;
            }

            this._renderButtons(table);

            table.appendTo(this.element);
        },

        _renderCalendar: function(){
            switch (this._mode) {
                case 'year': this._renderYears(); break;
                case 'month': this._renderMonths(); break;
                default: this._renderMonth();
            }
            this._initButtons();
        },

        _initButtons: function(){
            // Add actions
            var that = this, o = this.options,
                table = this.element.find('.calendar-grid');

            if (this._mode === 'day') {
                table.find('.btn-select-month').on('click', function(e){
                    e.preventDefault();
                    e.stopPropagation();
                    that._mode = 'month';
                    that._renderCalendar();
                });
                table.find('.btn-previous-month').on('click', function(e){
                    that._event = 'eventPrevious';
                    e.preventDefault();
                    e.stopPropagation();
                    that._month -= 1;
                    if (that._month < 0) {
                        that._year -= 1;
                        that._month = 11;
                    }
                    that._renderCalendar();
                });
                table.find('.btn-next-month').on('click', function(e){
                    that._event = 'eventNext';
                    e.preventDefault();
                    e.stopPropagation();
                    that._month += 1;
                    if (that._month === 12) {
                        that._year += 1;
                        that._month = 0;
                    }
                    that._renderCalendar();
                });
                table.find('.btn-previous-year').on('click', function(e){
                    that._event = 'eventPrevious';
                    e.preventDefault();
                    e.stopPropagation();
                    that._year -= 1;
                    that._renderCalendar();
                });
                table.find('.btn-next-year').on('click', function(e){
                    that._event = 'eventNext';
                    e.preventDefault();
                    e.stopPropagation();
                    that._year += 1;
                    that._renderCalendar();
                });
                table.find('.day a').on('click', function(e){
                    e.preventDefault();
                    e.stopPropagation();

                    if ($(this).parent().parent().hasClass('exclude')) {
                        return false;
                    }

                    var d = (new Date(that._year, that._month, parseInt($(this).html()))).format(that.options.format,null);
                    var d0 = (new Date(that._year, that._month, parseInt($(this).html())));

                    if (that.options.multiSelect) {
                        $(this).parent().parent().toggleClass("selected");

                        if ($(this).parent().parent().hasClass("selected")) {
                            that._addDate(d);
                        } else {
                            that._removeDate(d);
                        }
                    } else {
                        table.find('.day a').parent().parent().removeClass('selected');
                        $(this).parent().parent().addClass("selected");
                        that.element.data('_storage', []);
                        that._addDate(d);
                    }


                    //console.log(o.getDates);
                    //if (typeof o.getDates == 'string') {
                    //    window[o.getDates](that.element.data('_storage'));
                    //} else {
                    //    o.getDates(that.element.data('_storage'));
                    //}

                    if (typeof  o.dayClick === 'string') {
                        window[o.dayClick](d, d0);
                    } else {
                        o.dayClick(d, d0);
                    }
                });
            } else if (this._mode === 'month') {
                table.find('.month a').on('click', function(e){
                    that._event = 'eventNext';
                    e.preventDefault();
                    e.stopPropagation();
                    that._month = parseInt($(this).data('month'));
                    that._mode = 'day';
                    that._renderCalendar();
                });
                table.find('.btn-previous-year').on('click', function(e){
                    that._event = 'eventPrevious';
                    e.preventDefault();
                    e.stopPropagation();
                    that._year -= 1;
                    that._renderCalendar();
                });
                table.find('.btn-next-year').on('click', function(e){
                    that._event = 'eventNext';
                    e.preventDefault();
                    e.stopPropagation();
                    that._year += 1;
                    that._renderCalendar();
                });
                table.find('.btn-select-year').on('click', function(e){
                    that._event = 'eventNext';
                    e.preventDefault();
                    e.stopPropagation();
                    that._mode = 'year';
                    that._renderCalendar();
                });
            } else {
                table.find('.year a').on('click', function(e){
                    that._event = 'eventNext';
                    e.preventDefault();
                    e.stopPropagation();
                    that._year = parseInt($(this).data('year'));
                    that._mode = 'month';
                    that._renderCalendar();
                });
                table.find('.btn-previous-year').on('click', function(e){
                    that._event = 'eventPrevious';
                    e.preventDefault();
                    e.stopPropagation();
                    that._distance -= 10;
                    that._renderCalendar();
                });
                table.find('.btn-next-year').on('click', function(e){
                    that._event = 'eventNext';
                    e.preventDefault();
                    e.stopPropagation();
                    that._distance += 10;
                    that._renderCalendar();
                });
            }

            table.find('.calendar-btn-today').on('click', function(e){
                //that._event = 'eventNext';
                e.preventDefault();
                e.stopPropagation();
                that._mode = that.options.startMode;
                that.options.date = new Date();
                that._year = that.options.date.getFullYear();
                that._month = that.options.date.getMonth();
                that._day = that.options.date.getDate();
                that._renderCalendar();
            });
            table.find('.calendar-btn-clear').on('click', function(e){
                e.preventDefault();
                e.stopPropagation();
                that.options.date = new Date();
                that._year = that.options.date.getFullYear();
                that._month = that.options.date.getMonth();
                that._day = that.options.date.getDate();
                that.element.data('_storage', []);
                that._renderCalendar();
            });

        },

        _addDate: function(d){
            var index = this.element.data('_storage').indexOf(d);
            if (index < 0) {this.element.data('_storage').push(d);}
        },

        _removeDate: function(d){
            var index = this.element.data('_storage').indexOf(d);
            this.element.data('_storage').splice(index, 1);
        },

        _addDateExclude: function(d){
            var index = this.element.data('_exclude').indexOf(d);
            if (index < 0) {this.element.data('_exclude').push(d);}
        },

        _removeDateExclude: function(d){
            var index = this.element.data('_exclude').indexOf(d);
            this.element.data('_exclude').splice(index, 1);
        },

        setDate: function(d){
            var r;
            d = new Date(d);
            r = (new Date(d.getFullYear()+"/"+ (d.getMonth()+1)+"/"+ d.getDate())).format('yyyy-mm-dd');
            this._addDate(r);
            this._renderCalendar();
        },

        setDateExclude: function(d){
            var r;
            d = new Date(d);
            r = (new Date(d.getFullYear()+"/"+ (d.getMonth()+1)+"/"+ d.getDate())).format('yyyy-mm-dd');
            this._addDateExclude(r);
            this._renderCalendar();
        },

        getDate: function(index){
            return new Date(index !== undefined ? this.element.data('_storage')[index] : this.element.data('_storage')[0]).format(this.options.format);
        },

        getDates: function(){
            return this.element.data('_storage');
        },

        unsetDate: function(d){
            var r;
            d = new Date(d);
            r = (new Date(d.getFullYear()+"-"+ (d.getMonth()+1)+"-"+ d.getDate())).format('yyyy-mm-dd');
            this._removeDate(r);
            this._renderCalendar();
        },

        unsetDateExclude: function(d){
            var r;
            d = new Date(d);
            r = (new Date(d.getFullYear()+"-"+ (d.getMonth()+1)+"-"+ d.getDate())).format('yyyy-mm-dd');
            this._removeDateExclude(r);
            this._renderCalendar();
        },

        _destroy: function(){},

        _setOption: function(key, value){
            this._super('_setOption', key, value);
        }
    });
})( jQuery );
(function( $ ) {
    "use strict";

    $.widget("metro.carousel", {

        version: "3.0.0",

        options: {
            auto: true,
            period: 5000,
            duration: 1000,
            effect: 'slide', // slide, fade, switch, slowdown
            effectFunc: 'linear',
            direction: 'left',
            controls: true,
            controlNext: false,
            controlPrev: false,
            markers: true,
            stop: true,
            width: '100%',
            height: false,

            _slides: {},
            _currentIndex: 0,
            _interval: 0,
            _outPosition: 0
        },


        _create: function(){
            var that = this, o = this.options,
                element = this.element;

            $.each(element.data(), function(key, value){
                if (key in o) {
                    try {
                        o[key] = $.parseJSON(value);
                    } catch (e) {
                        o[key] = value;
                    }
                }
            });

            o._slides = element.find('.slide');

            var max_height = 0; //element.find('.slide:nth-child(1)').outerHeight();


            $.each(o._slides, function(){
                var oh, slide = $(this);

                oh = slide.outerHeight();

                if (oh > max_height) {max_height = oh;}
            });

            element.find('.slide').hide();
            element.find('.slide:nth-child(1)').show();

            element.css({
                'width': o.width,
                'height': o.height ? o.height : max_height
            });

            if (o._slides.length <= 1) {return;}

            if (o.markers) {
                this._markers();
            }

            if (o.controls) {
                this._controls();
            }

            if (o.stop) {
                element
                    .on('mouseenter', function(){
                        clearInterval(o._interval);
                    })
                    .on('mouseleave', function(){
                        if (that.options.auto) {that._autoStart();}// that.options.period;
                    });
            }

            //this._slideToSlide(0);
            if (o.auto) {
                this._autoStart();
            }

            element.data('carousel', this);

        },

        _autoStart: function(){
            var that = this, o = this.options;
            o._interval = setInterval(function(){
                if (o.direction === 'left') {
                    that._slideTo('next');
                } else {
                    that._slideTo('prior');
                }
            }, o.period);
        },

        _slideTo: function(direction){
            var carousel = this.element, that = this, o = this.options;
            var currentSlide = o._slides[o._currentIndex], nextSlide;

            if (direction === undefined) {direction = 'next';}

            if (direction === 'prior') {
                o._currentIndex -= 1;
                if (o._currentIndex < 0) {o._currentIndex = o._slides.length - 1;}

                o._outPosition = this.element.width();

            } else if (direction === 'next') {
                o._currentIndex += 1;
                if (o._currentIndex >= o._slides.length) {o._currentIndex = 0;}

                o._outPosition = -this.element.width();

            }

            nextSlide = o._slides[o._currentIndex];

            switch (this.options.effect) {
                case 'switch': this._effectSwitch(currentSlide, nextSlide); break;
                case 'slowdown': this._effectSlowdown(currentSlide, nextSlide, this.options.duration); break;
                case 'fade': this._effectFade(currentSlide, nextSlide, this.options.duration); break;
                default: this._effectSlide(currentSlide, nextSlide, this.options.duration);
            }

            carousel.find('.carousel-bullets a').each(function(){
                var index = $(this).data('num');
                if (index === o._currentIndex) {
                    $(this).addClass('bullet-on');
                } else {
                    $(this).removeClass('bullet-on');
                }
            });
        },

        _slideToSlide: function(slideIndex){
            var o = this.options,
                currentSlide = o._slides[o._currentIndex],
                nextSlide = o._slides[slideIndex];

            if (slideIndex > o._currentIndex) {
                o._outPosition = -this.element.width();
            } else {
                o._outPosition = this.element.width();
            }

            switch (this.options.effect) {
                case 'switch' : this._effectSwitch(currentSlide, nextSlide); break;
                case 'slowdown': this._effectSlowdown(currentSlide, nextSlide); break;
                case 'fade': this._effectFade(currentSlide, nextSlide); break;
                default : this._effectSlide(currentSlide, nextSlide);
            }

            o._currentIndex = slideIndex;
        },

        _controls: function(){
            var next, prev, that = this, element = this.element, o = this.options;

            next = $('<span/>').addClass('carousel-switch-next').html("&gt;");
            prev = $('<span/>').addClass('carousel-switch-prev').html("&lt;");

            if (o.controlNext) {
                next.html(o.controlNext);
            }

            if (o.controlPrev) {
                prev.html(o.controlPrev);
            }

            next.appendTo(element);
            prev.appendTo(element);

            if (o._slides.length > 1) {
                prev.on('click', function(){
                    that._slideTo('prior');
                });
                next.on('click', function(){
                    that._slideTo('next');
                });
            } else {
                next.hide();
                prev.hide();
            }
        },

        _markers: function () {
            var div, a, i, that = this, o = this.options;

            div = $('<div class="carousel-bullets" />');

            for (i = 0; i < o._slides.length; i++) {
                a = $('<a class="carousel-bullet" href="javascript:void(0)" data-num="' + i + '"></a>');
                if (i === 0) {
                    a.addClass('bullet-on');
                }
                a.appendTo(div);
            }


            div.find('a').on('click', function (e) {
                var $this = $(this),
                    index = $this.data('num');



                div.find('a').removeClass('bullet-on');
                $this.addClass('bullet-on');

                if (index === o._currentIndex) {
                    return false;
                }

                that._slideToSlide(index);

                e.preventDefault();
                e.stopPropagation();
            });

            div.appendTo(this.element);

        },


        _effectSwitch: function(currentSlide, nextSlide){
            $(currentSlide)
                .hide();
            $(nextSlide)
                .css({left: 0})
                .show();
            this.element.css({
                height: $(nextSlide).outerHeight()
            });
        },

        _effectSlide: function(currentSlide, nextSlide){
            var o = this.options;
            $(currentSlide)
                .animate({left: o._outPosition}, o.duration, o.effectFunc);
            $(nextSlide)
                .css('left', o._outPosition * -1)
                .show();

            this.element.css({
                height: $(nextSlide).outerHeight()
            });

            $(nextSlide).animate({left: 0}, o.duration, o.effectFunc);
        },

        _effectSlowdown: function(currentSlide, nextSlide){
            var o = this.options;
            var options = {
                'duration': o.duration,
                'easing': 'doubleSqrt'
            };
            $.easing.doubleSqrt = function(t) {
                return Math.sqrt(Math.sqrt(t));
            };

            $(currentSlide)
                .animate({left: o._outPosition}, options);


            $(nextSlide)
                .css('left', o._outPosition * -1)
                .show();

            this.element.css({
                height: $(nextSlide).outerHeight()
            });

            $(nextSlide).animate({left: 0}, options);
        },

        _effectFade: function(currentSlide, nextSlide){
            var o = this.options;

            $(currentSlide)
                .fadeOut(o.duration);
            $(nextSlide)
                .css({left: 0})
                .fadeIn(o.duration);
            this.element.css({
                height: $(nextSlide).outerHeight()
            });
        },


        _destroy: function(){

        },

        _setOption: function(key, value){
            this._super('_setOption', key, value);
        }
    });
})( jQuery );
(function ( $ ) {

    "use strict";

    $.widget( "metro.countdown" , {

        version: "3.0.0",

        options: {
            stop: false,
            days: false,
            hours: false,
            minutes: false,
            seconds: false,
            backgroundColor: 'bg-cyan',
            digitColor: 'fg-white',
            dividerColor: 'fg-dark',
            labelColor: 'fg-grayLight',
            labels: {
                'days': 'days',
                'hours': 'hours',
                'minutes': 'mins',
                'seconds': 'secs'
            },
            onTick: function(d, h, m, s){},
            onStop: function(){}
        },

        _interval: 0,
        _interval2: 0,
        _alarmOn: undefined,

        _create: function () {
            var that = this, element = this.element, o = this.options;

            $.each(element.data(), function(key, value){
                if (key in o) {
                    try {
                        o[key] = $.parseJSON(value);
                    } catch (e) {
                        o[key] = value;
                    }
                }
            });

            this._alarmOn = new Date();

            if (o.stop !== false) {
                this._alarmOn = new Date(o.stop);
            }

            var dm = 24*60*60*1000, hm = 60*60*1000, mm = 60*1000, sm = 1000;

            if (o.days !== false) {
                if (typeof this._alarmOn === 'object') {
                    this._alarmOn = this._alarmOn.getTime();
                }
                this._alarmOn = this._alarmOn + o.days*dm;
            }

            if (o.hours !== false) {
                if (typeof this._alarmOn === 'object') {
                    this._alarmOn = this._alarmOn.getTime();
                }
                this._alarmOn = this._alarmOn + o.hours*hm;
            }

            if (o.minutes !== false) {
                if (typeof this._alarmOn === 'object') {
                    this._alarmOn = this._alarmOn.getTime();
                }
                this._alarmOn = this._alarmOn + o.minutes*mm;
            }

            if (o.seconds !== false) {
                if (typeof this._alarmOn === 'object') {
                    this._alarmOn = this._alarmOn.getTime();
                }
                this._alarmOn = this._alarmOn + o.seconds*sm;
            }

            this._createDigits();

            element.find('.digit').text('0');

            that._tick();

            element.data('countdown', this);

        },

        _createDigits: function(){
            var element = this.element, o = this.options;
            var parts = ['days', 'hours', 'minutes', 'seconds'];
            var p, d;

            parts.map(function(v){
                p = $("<div/>").addClass('part ' + v).attr('data-day-text', o.labels[v]).appendTo(element);
                $("<div/>").addClass('digit').appendTo(p);
                $("<div/>").addClass('digit').appendTo(p);
                if (o.labelColor.isColor()) {
                    p.css({
                        color: o.labelColor
                    });
                } else {
                    p.addClass(o.labelColor);
                }

                if (o.backgroundColor.isColor()) {
                    p.find('.digit').css({
                        background: o.backgroundColor
                    });
                } else {
                    p.find('.digit').addClass(o.backgroundColor);
                }

                if (o.digitColor.isColor()) {
                    p.find('.digit').css({
                        color: o.digitColor
                    });
                } else {
                    p.find('.digit').addClass(o.digitColor);
                }

                if (v !== 'seconds') {
                    d = $("<div/>").addClass("divider").text(':').appendTo(element);
                    if (o.dividerColor.isColor()) {
                        d.css({'color': o.dividerColor});
                    } else {
                        d.addClass(o.dividerColor);
                    }
                }

            });

        },

        _blink: function(){
            this.element.toggleClass('tick');
        },

        _tick: function(){
            var that = this, o = this.options, element = this.element;
            var days = 24*60*60,
                hours = 60*60,
                minutes = 60;

            var left, d, h, m, s;

            this._interval2 = setInterval(function(){
                that._blink();
            }, 500);

            this._interval = setInterval(function(){
                left = Math.floor((that._alarmOn - (new Date())) / 1000);
                if (left < 0) {left = 0;}

                d = Math.floor(left / days);
                left -= d*days;
                that._update('days', d);

                if (d === 0) {
                    element.find('.part.days').addClass('disabled');
                }

                h = Math.floor(left / hours);
                left -= h*hours;
                that._update('hours', h);

                if (d === 0 && h === 0) {
                    element.find('.part.hours').addClass('disabled');
                }

                m = Math.floor(left / minutes);
                left -= m*minutes;
                that._update('minutes', m);

                if (d === 0 && h === 0 && m === 0) {
                    element.find('.part.minutes').addClass('disabled');
                }

                s = left;
                that._update('seconds', s);

                if (typeof o.onTick === 'string') {
                    window[o.onTick](d, h, m, s);
                } else {
                    o.onTick(d, h, m, s);
                }

                //that._blink();

                if (d === 0 && h === 0 && m === 0 && s === 0) {
                    element.find('.part').addClass('disabled');

                    if (typeof o.onStop === 'string') {
                        window[o.onStop]();
                    } else {
                        o.onStop();
                    }

                    that._stop('all');
                    that._trigger('alarm');
                    clearInterval(that._interval);
                }

            }, 1000);
        },

        _update: function(part, value){
            var element = this.element;
            var major_value = Math.floor(value/10)%10;
            var minor_value = value%10;
            var major_digit, minor_digit;

            major_digit = element.find("."+part+" .digit:eq(0)");
            minor_digit = element.find("."+part+" .digit:eq(1)");

            if (minor_value !== parseInt(minor_digit.text())) {
                minor_digit.toggleClass('scaleIn');
                setTimeout(function(){
                    minor_digit.text(minor_value).toggleClass('scaleIn');
                }, 500);
            }
            if (major_value !== parseInt(major_digit.text())) {
                major_digit.toggleClass('scaleIn');
                setTimeout(function(){
                    major_digit.text(major_value).toggleClass('scaleIn');
                }, 500);
            }
        },

        _stop: function(){
            clearInterval(this._interval);
            clearInterval(this._interval2);
        },

        _destroy: function () {
        },

        _setOption: function ( key, value ) {
            this._super('_setOption', key, value);
        }
    });

})( jQuery );
(function ( $ ) {

    "use strict";

    $.widget( "metro.datatable" , {

        version: "3.0.0",

        options: {
        },

        _create: function () {
            var that = this, element = this.element, o = this.options;

            $.each(element.data(), function(key, value){
                try {
                    o[key] = $.parseJSON(value);
                } catch (e) {
                    o[key] = value;
                }
            });

            if(jQuery().dataTable) {
                try {
                    element.dataTable(o);
                } catch (e) {

                }
            } else {
                alert('dataTable plugin required');
            }

            element.data('datatable', this);

        },

        _destroy: function () {
        },

        _setOption: function ( key, value ) {
            this._super('_setOption', key, value);
        }
    });

})( jQuery );
(function( $ ) {

    "use strict";

    $.widget("metro.datepicker", {

        version: "3.0.0",

        options: {
            format: "yyyy.mm.dd",
            preset: false,
            minDate: false,
            effect: 'fade',
            position: 'bottom',
            locale: window.METRO_CURRENT_LOCALE,
            weekStart: window.METRO_CALENDAR_WEEK_START,
            otherDays: false,
            exclude: false,
            buttons: false,
            buttonToday: true,
            buttonClear: true,
            condensedGrid: false,
            selected: function(d, d0){}
        },

        _calendar: undefined,

        _create: function(){
            var that = this,
                element = this.element, o = this.options,
                input = element.children("input"),
                button = element.children("button");

            //console.log(o);

            $.each(element.data(), function(key, value){
                //console.log(typeof key, key, value);

                if (key in o) {
                    try {
                        o[key] = $.parseJSON(value);
                    } catch (e) {
                        o[key] = value;
                    }
                }
            });

            this._createCalendar();

            input.attr('readonly', true);
            button.attr('type', 'button');

            button.on('click', function(e){
                e.stopPropagation();
                if (that._calendar.css('display') === 'none') {
                    that._show();
                } else {
                    that._hide();
                }
            });

            element.on('click', function(e){
                e.stopPropagation();
                if (that._calendar.css('display') === 'none') {
                    that._show();
                } else {
                    that._hide();
                }
            });

            $('html').on('click', function(){
                $(".calendar-dropdown").hide();
            });

            element.data('datepicker', this);

        },

        _createCalendar: function(){
            var _calendar, that = this, element = this.element, o = this.options;

            _calendar = $("<div/>").css({
                position: 'absolute',
                display: 'none',
                'max-width': 220,
                'z-index': 1000

            }).addClass('calendar calendar-dropdown').appendTo(element);

            //if (o.date != undefined) {
                //_calendar.data('date', o.date);
            //}


            _calendar.calendar({
                multiSelect: false,
                format: o.format,
                buttons: false,
                buttonToday: false,
                buttonClear: false,
                locale: o.locale,
                otherDays: o.otherDays,
                weekStart: o.weekStart,
                condensedGrid: o.condensedGrid,
                exclude: o.exclude,
                date: o.preset ? o.preset : new Date(),
                minDate: o.minDate,
                dayClick: function(d, d0){
                    //console.log(d, d0);
                    _calendar.calendar('setDate', d0);
                    that.element.children("input[type=text]").val(d);
                    that.options.selected(d, d0);
                    that._hide();
                }
            });

            if (o.preset !== false) {
                //console.log(o.preset);
                _calendar.calendar('setDate', o.preset);
                element.find("input, .datepicker-output").val(_calendar.calendar('getDate'));
            }

            // Set position
            switch (this.options.position) {
                case 'top': _calendar.css({top: (0-_calendar.height()), left: 0}); break;
                default: _calendar.css({top: '100%', left: 0});
            }

            this._calendar = _calendar;
        },

        _show: function(){
            if (this.options.effect === 'slide') {
                $(".calendar-dropdown").slideUp('fast');
                this._calendar.slideDown('fast');
            } else if (this.options.effect === 'fade') {
                $(".calendar-dropdown").fadeOut('fast');
                this._calendar.fadeIn('fast');
            } else {
                $(".calendar-dropdown").hide();
                this._calendar.show();
            }
        },
        _hide: function(){
            if (this.options.effect === 'slide') {
                this._calendar.slideUp('fast');
            } else if (this.options.effect === 'fade') {
                this._calendar.fadeOut('fast');
            } else {
                this._calendar.hide();
            }
        },

        _destroy: function(){
        },

        _setOption: function(key, value){
            this._super('_setOption', key, value);
        }
    });
})( jQuery );



(function ( $ ) {

    "use strict";

    $.widget( "metro.dialog" , {

        version: "3.0.0",

        options: {
            modal: false,
            overlay: false,
            overlayColor: 'default',
            type: 'default', // success, alert, warning, info
            place: 'center', // center, top-left, top-center, top-right, center-left, center-right, bottom-left, bottom-center, bottom-right
            position: 'default',
            content: false,
            hide: false,
            width: 'auto',
            height: 'auto',
            background: 'default',
            color: 'default',
            closeButton: false,
            windowsStyle: false,

            _interval: undefined,
            _overlay: undefined,

            onDialogOpen: function(dialog){},
            onDialogClose: function(dialog){}
        },

        _create: function () {
            var that = this, element = this.element, o = this.options;

            $.each(element.data(), function(key, value){
                if (key in o) {
                    try {
                        o[key] = $.parseJSON(value);
                    } catch (e) {
                        o[key] = value;
                    }
                }
            });

            if (o.overlay) {
                this._createOverlay();
            }
            this._createDialog();

            element.data('dialog', this);
        },

        _createOverlay: function(){
            var that = this, element = this.element, o = this.options;
            var overlay = $('body').find('.dialog-overlay');

            if (overlay.length === 0) {
                overlay = $("<div/>").addClass('dialog-overlay');
            }

            if (o.overlayColor) {
                if (o.overlayColor.isColor()) {
                    overlay.css({
                        background: o.overlayColor
                    });
                } else {
                    overlay.addClass(o.overlayColor);
                }
            }

            o._overlay = overlay;
        },

        _createDialog: function(){
            var that = this, element = this.element, o = this.options;

            element.addClass('dialog');

            if (o.type !== 'default') {
                element.addClass(o.type);
            }

            if (o.windowsStyle) {
                o.width = 'auto';

                element.css({
                    left: 0,
                    right: 0
                });
            }

            if (o.background !== 'default') {
                if (o.background.isColor()) {
                    element.css({
                        background: o.background
                    });
                } else {
                    element.addClass(o.background);
                }
            }

            if (o.color !== 'default') {
                if (o.color.isColor()) {
                    element.css({
                        color: o.color
                    });
                } else {
                    element.addClass(o.color);
                }
            }

            element.css({
                width: o.width,
                height: o.height
            });

            if (o.closeButton) {
                $("<span/>").addClass('dialog-close-button').appendTo(element).on('click', function(){
                    that.close();
                });
            }

            element.hide();
        },

        _setPosition: function(){
            var that = this, element = this.element, o = this.options;
            var width = element.width(),
                height = element.height();

            element.css({
                left: o.windowsStyle === false ? ( $(window).width() - width ) / 2 : 0,
                top: ( $(window).height() - height ) / 2
            });
        },

        open: function(){
            var that = this, element = this.element, o = this.options;
            var overlay;

            this._setPosition();

            element.data('opened', true);

            if (o.overlay) {
                overlay = o._overlay;
                overlay.appendTo('body').show();
            }

            element.fadeIn();

            if (typeof o.onDialogOpen === 'string') {
                window[o.onDialogOpen](element);
            } else {
                o.onDialogOpen(element);
            }

            if (o.hide && parseInt(o.hide) > 0) {
                o._interval = setTimeout(function(){
                    that.close();
                }, parseInt(o.hide));
            }
        },

        close: function(){
            var that = this, element = this.element, o = this.options;

            clearInterval(o._interval);

            if (o.overlay) {
                $('body').find('.dialog-overlay').remove();
            }

            element.data('opened', false);

            element.fadeOut();

            if (typeof o.onDialogClose === 'string') {
                window[o.onDialogClose](element);
            } else {
                o.onDialogClose(element);
            }
        },

        _destroy: function () {
        },

        _setOption: function ( key, value ) {
            this._super('_setOption', key, value);
        }
    });
})( jQuery );
(function( $ ) {

    "use strict";

    $.widget("metro.dropdown", {

        version: "3.0.0",

        options: {
            effect: window.METRO_SHOW_TYPE,
			toggleElement: false,
            noClose: false
        },

        _create: function(){
            var  that = this, element = this.element, o = this.options,
                 menu = this.element,
                 name = this.name,
                 parent = this.element.parent();

            var toggle;

            $.each(element.data(), function(key, value){
                if (key in o) {
                    try {
                        o[key] = $.parseJSON(value);
                    } catch (e) {
                        o[key] = value;
                    }
                }
            });

            toggle = o.toggleElement ? $(o.toggleElement) : parent.children('.dropdown-toggle').length > 0 ? parent.children('.dropdown-toggle') : parent.children('a:nth-child(1');

            if (METRO_SHOW_TYPE !== undefined) {
                this.options.effect = METRO_SHOW_TYPE;
            }

            toggle.on('click.'+name, function(e){
                parent.siblings(parent[0].tagName).removeClass("active-container");
                $(".active-container").removeClass("active-container");

                if (menu.css('display') === 'block' && !menu.hasClass('keep-open')) {
                    that._close(menu);
                } else {
                    $('[data-role=dropdown]').each(function(i, el){
                        if (!menu.parents('[data-role=dropdown]').is(el) && !$(el).hasClass('keep-open') && $(el).css('display') === 'block') {
                            that._close(el);
                        }
                    });
                    if (menu.hasClass('horizontal')) {
                        menu.css({
                            'visibility': 'hidden',
                            'display': 'block'
                        });
                        var item_length = $(menu.children('li')[0]).outerWidth();
                        //var item_length2 = $(menu.children('li')[0]).width();
                        menu.css({
                            'visibility': 'visible',
                            'display': 'none'
                        });
                        var menu_width = menu.children('li').length * item_length + (menu.children('li').length - 1);
                        menu.css('width', menu_width);
                    }
                    that._open(menu);
                    parent.addClass("active-container");
                }
                e.preventDefault();
                e.stopPropagation();
            });

            if (o.noClose === true) {
                element.on('click', function (e) {
                   // e.preventDefault();
                    e.stopPropagation();
                });
            }

            $(menu).find('li.disabled a').on('click', function(e){
                e.preventDefault();
            });

            $(document).on('click', function(e){
                $('[data-role=dropdown]').each(function(i, el){
                    if (!$(el).hasClass('keep-open') && $(el).css('display')==='block') {
                        $(el).hide();
                    }
                });
            });

            element.data('dropdown', this);
        },

        _open: function(el){
            switch (this.options.effect) {
                case 'fade': $(el).fadeIn('fast'); break;
                case 'slide': $(el).slideDown('fast'); break;
                default: $(el).show();
            }
            this._trigger("onOpen", null, el);
        },

        _close: function(el){
            switch (this.options.effect) {
                case 'fade': $(el).fadeOut('fast'); break;
                case 'slide': $(el).slideUp('fast'); break;
                default: $(el).hide();
            }
            this._trigger("onClose", null, el);
        },

        _destroy: function(){
        },

        _setOption: function(key, value){
            this._super('_setOption', key, value);
        }
    });
})( jQuery );

(function ( $ ) {

    "use strict";

    $.widget( "metro.fitImage" , {

        version: "3.0.0",

        options: {
            shadow: false,
            overlay: false,
            type: 'default',
            frameColor: 'default',
            format: 'hd' // 'sd'
        },

        _create: function () {
            var element = this.element, o = this.options;
            var parent = element.parent();
            var i_w, i_h, p_w, p_h;
            var div, src = element.attr('src');

            $.each(element.data(), function(key, value){
                if (key in o) {
                    try {
                        o[key] = $.parseJSON(value);
                    } catch (e) {
                        o[key] = value;
                    }
                }
            });

            $("<img/>")
                .attr('src', src)
                .load(function(){
                    i_w = this.width;
                    i_h = this.height;
                }).remove();

            var image_container = $("<div/>").addClass('image-container').css('width', '100%').appendTo(parent);
            var image_frame = $("<div/>").addClass('frame').appendTo(image_container);

            p_w = image_frame.innerWidth();
            p_h = image_frame.innerHeight();

            switch (o.format) {
                case 'sd': p_h = 3 * p_w / 4; break;
                case 'square': p_h = p_w; break;
                case 'cycle': p_h = p_w; break;
                case 'fill-h': p_h = "100%"; image_container.css('height', '100%'); break;
                case 'fill': p_h = "100%"; image_container.css('height', '100%'); break;
                default: p_h = 9 * p_w / 16;
            }

            div = $("<div/>").css({
                'width': '100%',
                'height': p_h,
                'background-image': 'url('+src+')',
                'background-size': 'cover',
                'background-repeat': 'no-repeat',
                'border-radius': o.format === 'cycle' ? '50%' : '0'
            });



            if (o.frameColor !== 'default') {
                if (o.frameColor.isUrl()) {
                    image_frame.css('background-color', o.frameColor);
                } else {
                    image_frame.addClass(o.frameColor);
                }
            }
            if (o.overlay !== false) {
                var overlay = $("<div/>").addClass('image-overlay').html(o.overlay).appendTo(image_container);
            }
            if (o.shadow !== false) {
                image_container.addClass('block-shadow');
            }
            div.appendTo(image_frame);

            switch (o.type) {
                case 'diamond': {
                    image_container.addClass('diamond'); div.addClass('image-replacer'); break;
                }
                case 'bordered': {
                    image_container.addClass('bordered'); break;
                }
                case 'polaroid': {
                    image_container.addClass('polaroid'); break;
                }
                case 'handing': {
                    image_container.addClass('handing'); break;
                }
                case 'handing-ani': {
                    image_container.addClass('handing ani'); break;
                }
                case 'handing-ani-hover': {
                    image_container.addClass('handing ani-hover'); break;
                }
            }

            image_container.addClass('image-format-'+ o.format);
            //element.css('display', 'none');
            element.remove();

        },

        _destroy: function () {
        },

        _setOption: function ( key, value ) {
            this._super('_setOption', key, value);
        }
    });
})( jQuery );
(function( $ ) {

    "use strict";

    $.widget("metro.hint", {

        version: "3.0.0",

        options: {
            hintPosition: "auto", // bottom, top, left, right, auto
            hintBackground: '#FFFCC0',
            hintColor: '#000000',
            hintMaxSize: 200,
            hintMode: 'default',

            _hint: undefined
        },

        _create: function(){
            var that = this, element = this.element;
            var o = this.options;


            this.element.on('mouseenter', function(e){
                $(".hint, .hint2").remove();
                that.createHint();
                o._hint.show();
                e.preventDefault();
            });

            this.element.on('mouseleave', function(e){
                o._hint.hide().remove();
                e.preventDefault();
            });

            //element.data('hint', this);

        },

        createHint: function(){
            var that = this, element = this.element,
                hint = element.data('hint').split("|"),
                o = this.options;

            var _hint;

            $.each(element.data(), function(key, value){
                if (key in o) {
                    try {
                        o[key] = $.parseJSON(value);
                    } catch (e) {
                        o[key] = value;
                    }
                }
            });

            if (element[0].tagName === 'TD' || element[0].tagName === 'TH') {
                var wrp = $("<div/>").css("display", "inline-block").html(element.html());
                element.html(wrp);
                element = wrp;
            }

            var hint_title = hint.length > 1 ? hint[0] : false;
            var hint_text = hint.length > 1 ? hint[1] : hint[0];


            _hint = $("<div/>").appendTo('body');
            if (o.hintMode === 2) {
                _hint.addClass('hint2');
            } else {
                _hint.addClass('hint');
            }

            if (hint_title) {
                $("<div/>").addClass("hint-title").html(hint_title).appendTo(_hint);
            }
            $("<div/>").addClass("hint-text").html(hint_text).appendTo(_hint);

            _hint.addClass(o.position);

            if (o.hintShadow) {_hint.addClass("shadow");}
            if (o.hintBackground) {
                if (o.hintBackground.isColor()) {
                    _hint.css("background-color", o.hintBackground);
                } else {
                    _hint.addClass(o.hintBackground);
                }
            }
            if (o.hintColor) {
                if (o.hintColor.isColor()) {
                    _hint.css("color", o.hintColor);
                } else {
                    _hint.addClass(o.hintColor);
                }
            }

            if (o.hintMaxSize > 0) {
                _hint.css({
                    'max-width': o.hintMaxSize
                });
            }

            //if (o.hintMode !== 'default') {
            //    _hint.addClass(o.hintMode);
            //}

            if (o.hintPosition === 'top') {
                _hint.addClass('top');
                _hint.css({
                    top: element.offset().top - $(window).scrollTop() - _hint.outerHeight() - 20,
                    left: o.hintMode === 2 ? element.offset().left + element.outerWidth()/2 - _hint.outerWidth()/2  - $(window).scrollLeft(): element.offset().left - $(window).scrollLeft()
                });
            } else if (o.hintPosition === 'right') {
                _hint.addClass('right');
                _hint.css({
                    top: o.hintMode === 2 ? element.offset().top + element.outerHeight()/2 - _hint.outerHeight()/2 - $(window).scrollTop() - 10 : element.offset().top - 10 - $(window).scrollTop(),
                    left: element.offset().left + element.outerWidth() + 15 - $(window).scrollLeft()
                });
            } else if (o.hintPosition === 'left') {
                _hint.addClass('left');
                _hint.css({
                    top: o.hintMode === 2 ? element.offset().top + element.outerHeight()/2 - _hint.outerHeight()/2 - $(window).scrollTop() - 10 : element.offset().top - 10 - $(window).scrollTop(),
                    left: element.offset().left - _hint.outerWidth() - 10 - $(window).scrollLeft()
                });
            } else {
                _hint.addClass('bottom');
                _hint.css({
                    top: element.offset().top - $(window).scrollTop() + element.outerHeight(),
                    left: o.hintMode === 2 ? element.offset().left + element.outerWidth()/2 - _hint.outerWidth()/2  - $(window).scrollLeft(): element.offset().left - $(window).scrollLeft()
                });
                console.log(element.offset().left);
            }

            o._hint = _hint;
        },

        _destroy: function(){
        },

        _setOption: function(key, value){
            this._super('_setOption', key, value);
        }
    });
})( jQuery );




(function( $ ) {
    "use strict";

    $.widget("metro.input", {

        version: "3.0.0",

        options: {
            showLabelOnValue: false
        },

        _create: function(){
            var element = this.element, o = this.options;

            $.each(element.data(), function(key, value){
                if (key in o) {
                    try {
                        o[key] = $.parseJSON(value);
                    } catch (e) {
                        o[key] = value;
                    }
                }
            });

            if (element.hasClass('file')) {this._createInputFile();}
            if (element.hasClass('text')) {this._createInputText();}
            if (element.hasClass('password')) {this._createInputText();}
            if (element.hasClass('select')) {this._createInputSelect();}
            if (element.hasClass('textarea')) {this._createInputTextarea();}
            if (element.hasClass('modern')) {this._createInputModern();}

            element.data('input', this);

        },

        _createInputModern: function(){
            var element = this.element;
            var input = element.find("input");
            var placeholder = element.find(".placeholder");

            input.on("blur", function(){
                if (input.val() !== "") {
                    placeholder.css({display: "none"});
                } else {
                    placeholder.css({display: "block"});
                }
            });
        },

        _createInputFile: function(){
            var element = this.element;
            var wrapper, button, input;
            wrapper = $("<input type='text' class='input-file-wrapper' readonly style='z-index: 1; cursor: default;'>");
            button = element.children('.button');
            input = element.children('input[type="file"]');
            input.css('z-index', 0);
            wrapper.insertAfter(input);
            input.attr('tabindex', '-1');
            button.attr('type', 'button');

            input.on('change', function(){
                var val = $(this).val();
                if (val !== '') {
                    wrapper.val(val.replace(/.+[\\\/]/, ""));
                    wrapper.attr('title', val.replace(/.+[\\\/]/, ""));
                }
            });

            element.on('click', '.button, .input-file-wrapper', function(){
                input.trigger('click');
            });
        },

        _createInputText: function(){
            var element = this.element;
            var helper_clear = element.find('.helper-button.clear');
            var helper_reveal = element.find('.helper-button.reveal');
            var input = element.find('input');
            var helpers = element.find('.helper-button');
            var buttons = element.find('.button');
            var padding = 0;

            $.each(buttons, function(){
                var b = $(this);
                padding += b.outerWidth();
            });

            input.css({
                'padding-right': padding + 5
            });

            helpers
                .attr('tabindex', -1)
                .attr('type', 'button');

            if (helper_clear) {
                helper_clear.on('click', function(){
                    input.val('').focus();
                });
            }
            if (helper_reveal && element.hasClass('password')) {
                helper_reveal
                    .on('mousedown', function(){input.attr('type', 'text');})
                    .on('mouseup', function(){input.attr('type', 'password').focus();});
            }
        },

        _createInputSelect: function(){

        },

        _createInputTextarea: function(){

        },

        _destroy: function(){

        },

        _setOption: function(key, value){
            this._super('_setOption', key, value);
        }
    });
})( jQuery );
(function ( $ ) {

    "use strict";

    $.widget( "metro.listview" , {

        version: "3.0.0",

        options: {
            onExpand: function(group){},
            onCollapse: function(group){},
            onActivate: function(list){}
        },

        _create: function () {
            var that = this, element = this.element, o = this.options;

            $.each(element.data(), function(key, value){
                if (key in o) {
                    try {
                        o[key] = $.parseJSON(value);
                    } catch (e) {
                        o[key] = value;
                    }
                }
            });

            this._initList();
            this._createEvents();

            element.data('listview', this);

        },

        _initList: function(){
            var that = this, element = this.element, o = this.options;
            var groups = element.find('.list-group');

            $.each(groups, function(){
                var group = $(this);
                if (group.hasClass('collapsed')) {
                    group.find('.list-group-content').hide();
                }
            });

        },

        _createEvents: function(){
            var that = this, element = this.element, o = this.options;

            element.on('click', '.list-group-toggle', function(e){
                var toggle = $(this), parent = toggle.parent();

                if (toggle.parent().hasClass('keep-open')) {
                    return;
                }

                parent.toggleClass('collapsed');

                if (!parent.hasClass('collapsed')) {
                    toggle.siblings('.list-group-content').slideDown('fast');
                    if (typeof o.onExpand === 'string') {
                        window[o.onExpand](parent);
                    } else {
                        o.onExpand(parent);
                    }
                } else {
                    toggle.siblings('.list-group-content').slideUp('fast');
                    if (typeof o.onCollapse === 'string') {
                        window[o.onCollapse](parent);
                    } else {
                        o.onCollapse(parent);
                    }
                }
                e.preventDefault();
                e.stopPropagation();
            });

            element.on('click', '.list', function(e){
                var list = $(this);

                element.find('.list').removeClass('active');
                list.addClass('active');
                if (typeof o.onActivate === 'string') {
                    window[o.onActivate](list);
                } else {
                    o.onActivate(list);
                }
                e.preventDefault();
                e.stopPropagation();
            });
        },

        _destroy: function () {
        },

        _setOption: function ( key, value ) {
            this._super('_setOption', key, value);
        }
    });

})( jQuery );
(function($) {

    "use strict";

	var _notify_container = false;
	var _notifies = [];
	
	var Notify = {
		
		_container: null,
		_notify: null,
		_timer: null,

        version: "3.0.0",

		options: {
			icon: '', // to be implemented
			caption: '',
			content: '',
			shadow: true,
			width: 'auto',
			height: 'auto',
			style: false, // {background: '', color: ''}
			position: 'right', //right, left
			timeout: 3000,
            keepOpen: false,
            type: 'default' //default, success, alert, info, warning
		},
		
		init: function(options) {
			this.options = $.extend({}, this.options, options);
			this._build();
			return this;
		},
		
		_build: function() {
            var that = this, o = this.options;

			this._container = _notify_container || $("<div/>").addClass("notify-container").appendTo('body');
			_notify_container = this._container;

			if (o.content === '' || o.content === undefined) {return false;}
			
			this._notify = $("<div/>").addClass("notify");

            if (o.type !== 'default') {
                this._notify.addClass(o.type);
            }
			
			if (o.shadow) {this._notify.addClass("shadow");}
       		if (o.style && o.style.background !== undefined) {this._notify.css("background-color", o.style.background);}
        	if (o.style && o.style.color !== undefined) {this._notify.css("color", o.style.color);}

            // add Icon
            if (o.icon !== '') {
                var icon = $(o.icon).addClass('notify-icon').appendTo(this._notify);
            }

			// add title
   	    	if (o.caption !== '' && o.caption !== undefined) {
   	    	    $("<div/>").addClass("notify-title").html(o.caption).appendTo(this._notify);
   	    	}
   	    	// add content
   	    	if (o.content !== '' && o.content !== undefined) {
   	    	    $("<div/>").addClass("notify-text").html(o.content).appendTo(this._notify);
   	    	}

            // add closer
            var closer = $("<span/>").addClass("notify-closer").appendTo(this._notify);
            closer.on('click', function(){
                that.close(0);
            });

			if (o.width !== 'auto') {this._notify.css('min-width', o.width);}
	        if (o.height !== 'auto') {this._notify.css('min-height', o.height);}
			
			this._notify.hide().appendTo(this._container).fadeIn('slow');
        	_notifies.push(this._notify);

            if (!o.keepOpen) {
                this.close(o.timeout);
            }
			
		},
		
		close: function(timeout) {
            var self = this;

            if(timeout === undefined) {
                return this._hide();
            }

            setTimeout(function() {
                self._hide();
            }, timeout);

			return this;
		},
		
		_hide: function() {
            var that = this;

			if(this._notify !== undefined) {
        	   	this._notify.fadeOut('slow', function() {
					$(this).remove();
					_notifies.splice(_notifies.indexOf(that._notify), 1);
				});
				return this;
			} else {
				return false;
			}
		},
		
		closeAll: function() {
			_notifies.forEach(function(notEntry) {
				notEntry.hide('slow', function() {
					notEntry.remove();
					_notifies.splice(_notifies.indexOf(notEntry), 1);
				});
			});
			return this;
		}
	};
	
	$.Notify = function(options) {
		return Object.create(Notify).init(options);
	};

	$.Notify.show = function(message, title, icon) {
		return $.Notify({
       	    content: message,
       	    caption: title,
            icon: icon
       	});
    };
	
})(jQuery);

(function( $ ) {

    "use strict";

    $.widget("metro.panel", {

        version: "3.0.0",

        options: {
        },

        _create: function(){
            var element = this.element, o = this.options;

            $.each(element.data(), function(key, value){
                if (key in o) {
                    try {
                        o[key] = $.parseJSON(value);
                    } catch (e) {
                        o[key] = value;
                    }
                }
            });

            if (!element.hasClass('collapsible')) {element.addClass('collapsible');}

            if (element.hasClass("collapsible")) {
                var toggle = element.children(".heading");
                var content = element.children(".content");

                toggle.on("click", function(){
                    if (element.hasClass("collapsed")) {
                        content.slideDown('fast', function(){
                            element.removeClass('collapsed');
                        });
                    } else {
                        content.slideUp('fast', function(){
                            element.addClass('collapsed');
                        });
                    }

                });
            }

            element.data('panel', this);

        },

        _destroy: function(){

        },

        _setOption: function(key, value){
            this._super('_setOption', key, value);
        }
    });
})( jQuery );
(function ( $ ) {

    "use strict";

    $.widget( "metro.widget" , {

        version: "3.0.0",

        options: {
            someValue: null
        },

        _create: function () {
            var that = this, element = this.element, o = this.options;

            $.each(element.data(), function(key, value){
                if (key in o) {
                    try {
                        o[key] = $.parseJSON(value);
                    } catch (e) {
                        o[key] = value;
                    }
                }
            });

            element.data('widget', this);

        },

        _destroy: function () {
        },

        _setOption: function ( key, value ) {
            this._super('_setOption', key, value);
        }
    });

})( jQuery );
(function( $ ) {

    "use strict";

    $.widget("metro.popover", {

        version: "3.0.0",

        options: {
            popoverText: '',
            popoverTimeout: 3000,
            popoverPosition: 'top', //top, bottom, left, right
            popoverBackground: 'bg-cyan',
            popoverColor: 'fg-white',
            popoverMode: 'none', //click, hover,
            popoverShadow: true
        },

        popover: {},

        _create: function(){
            var element = this.element;

            this.createPopover();
            element.data('popover', this);

        },

        createPopover: function(){
            var that = this, element,
                o = this.options;

            element = this.element;

            $.each(element.data(), function(key, value){
                if (key in o) {
                    try {
                        o[key] = $.parseJSON(value);
                    } catch (e) {
                        o[key] = value;
                    }
                }
            });

            var popover, content_container, marker_class;

            popover = $("<div/>").addClass("popover").appendTo('body').hide();
            $("<div/>").appendTo(popover);

            if (o.popoverShadow) {
                popover.addClass("popover-shadow");
            }

            if (o.popoverBackground) {
                if (o.popoverBackground[0] === '#') {
                    popover.css('background-color', o.popoverBackground);
                } else {
                    popover.addClass(o.popoverBackground);
                }
            }
            if (o.popoverColor) {
                if (o.popoverColor[0] === '#') {
                    popover.css('color', o.popoverColor);
                } else {
                    popover.addClass(o.popoverColor);
                }
            }

            switch (o.popoverPosition) {
                case 'left': marker_class = 'marker-on-right'; break;
                case 'right': marker_class = 'marker-on-left'; break;
                case 'bottom': marker_class = 'marker-on-top'; break;
                default: marker_class = 'marker-on-bottom';
            }

            popover.css({
                position: 'fixed'
            });

            popover.addClass(marker_class);

            this.popover = popover;

            this.setText(o.popoverText);

            element.on(o.popoverMode, function(e){
                if (!popover.data('visible')) {that.show();}
            });

            $(window).scroll(function(){
                //that.popover.hide();
                if (that.popover.data('visible')) {
                    that.setPosition();
                }
            });

        },

        setPosition: function(){
            var o = this.options, popover = this.popover, element = this.element;

            if (o.popoverPosition === 'top') {
                popover.css({
                    top: element.offset().top - $(window).scrollTop() - popover.outerHeight() - 10,
                    left: element.offset().left + element.outerWidth()/2 - popover.outerWidth()/2  - $(window).scrollLeft()
                });
            } else if (o.popoverPosition === 'bottom') {
                popover.css({
                    top: element.offset().top - $(window).scrollTop() + element.outerHeight() + 10,
                    left: element.offset().left + element.outerWidth()/2 - popover.outerWidth()/2  - $(window).scrollLeft()
                });
            } else if (o.popoverPosition === 'right') {
                popover.css({
                    top: element.offset().top + element.outerHeight()/2 - popover.outerHeight()/2 - $(window).scrollTop(),
                    left: element.offset().left + element.outerWidth() - $(window).scrollLeft() + 10
                });
            } else if (o.popoverPosition === 'left') {
                popover.css({
                    top: element.offset().top + element.outerHeight()/2 - popover.outerHeight()/2 - $(window).scrollTop(),
                    left: element.offset().left - popover.outerWidth() - $(window).scrollLeft() - 10
                });
            }
            return this;
        },

        setText: function(text){
            this.popover.children('div').html(text);
        },

        show: function(){
            var o = this.options, popover = this.popover;

            this.setPosition();

            popover.fadeIn(function(){
                popover.data('visible', true);
                setTimeout(function(){
                    popover.fadeOut(
                        function(){popover.data('visible', false);}
                    );
                }, o.popoverTimeout);
            });
        },

        _destroy: function(){
        },

        _setOption: function(key, value){
            this._super('_setOption', key, value);
        }
    });
})( jQuery );
(function ( $ ) {

    "use strict";

    $.widget( "metro.preloader" , {

        version: "3.0.0",

        options: {
            type: 'ring',
            style: 'light'
        },

        _create: function () {
            var that = this, element = this.element, o = this.options;

            $.each(element.data(), function(key, value){
                if (key in o) {
                    try {
                        o[key] = $.parseJSON(value);
                    } catch (e) {
                        o[key] = value;
                    }
                }
            });

            this._createStructure();

            element.data('preloader', this);

        },

        _createRing: function(){
            var that = this, element = this.element, o = this.options;
            var i, wrap, circle;

            for(i = 0; i < 5 ; i++) {
                wrap = $("<div/>").addClass('wrap').appendTo(element);
                circle = $("<div/>").addClass('circle').appendTo(wrap);
            }
        },

        _createMetro: function(){
            var that = this, element = this.element, o = this.options;
            var i, circle;

            for(i = 0; i < 5 ; i++) {
                circle = $("<div/>").addClass('circle').appendTo(element);
            }
        },

        _createStructure: function(){
            var that = this, element = this.element, o = this.options;

            element.addClass("preloader-"+o.type);
            if (o.style !== 'light') {
                element.addClass(o.style + '-style');
            }

            element.html('');

            switch (o.type) {
                case 'ring': this._createRing(); break;
                case 'metro': this._createMetro(); break;
            }
        },

        _destroy: function () {
        },

        _setOption: function ( key, value ) {
            this._super('_setOption', key, value);
        }
    });

})( jQuery );
(function ( $ ) {

    "use strict";

    $.widget( "metro.presenter" , {

        version: "3.0.0",

        options: {
            someValue: null
        },

        _create: function () {
            var that = this, element = this.element, o = this.options;

            $.each(element.data(), function(key, value){
                if (key in o) {
                    try {
                        o[key] = $.parseJSON(value);
                    } catch (e) {
                        o[key] = value;
                    }
                }
            });

            element.data('presenter', this);

        },

        _destroy: function () {
        },

        _setOption: function ( key, value ) {
            this._super('_setOption', key, value);
        }
    });

})( jQuery );
// TODO - add color bar parts

(function ( $ ) {

    "use strict";

    $.widget( "metro.progressBar" , {

        version: "3.0.0",

        options: {
            color: 'default',
            colors: false,
            value: 0
        },

        colorsDim: {},

        _create: function () {
            var that = this, element = this.element, o = this.options;
            var bar = element.children('.bar:last-child');

            $.each(element.data(), function(key, value){
                if (key in o) {
                    try {
                        o[key] = $.parseJSON(value);
                    } catch (e) {
                        o[key] = value;
                    }
                }
            });

            if (bar.length === 0) {
                bar = $('<div/>').addClass('bar').appendTo(element);
            }

            if (o.colors) {
                var p = 0;
                $.each(o.colors, function(c,v){
                    that.colorsDim[c] = [p,v];
                    p = v + 1;
                });
            }

            this.progress(o.value);
            this.color(o.color);

            element.data('progressBar', this);

        },

        color: function(value){
            var element = this.element, o = this.options;
            var bar = element.children('.bar:last-child');
            var isOk  = /(^#[0-9A-F]{6}$)|(^#[0-9A-F]{3}$)/i.test(value);

            if (isOk) {
                bar.css({
                    'background-color': value
                });
            } else {
                bar.removeClass(function(index, css){
                    return (css.match (/(^|\s)bg-\S+/g) || []).join(' ');
                }).addClass(value);
            }

            o.color = value;
        },

        progress: function(value){
            if (value !== undefined) {
                var element = this.element, o = this.options, colors = this.colorsDim;
                var bar = element.children('.bar:last-child');
                var that = this, gradient = [];

                if (parseInt(value) < 0) {
                    return;
                }


                if (o.colors) {

                    $.each(colors, function (c, v) {
                        if (value >= v[0] && value <= v[1]) {
                            that.color(c);
                            return true;
                        }
                    });
                    //$.each(o.colors, function(c, v){
                    //    gradient.push(c + " " + v + "%");
                    //});
                    //console.log(gradient.join(","));
                    //
                    //bar.css({
                    //    'background': "linear-gradient(to right, " + gradient.join(",") + ")"
                    //});
                }

                o.value = value;

                bar.animate({
                    width: o.value + '%'
                }, 100);
            } else {
                return this.options.value;
            }
        },

        _destroy: function () {
        },

        _setOption: function ( key, value ) {
            this._super('_setOption', key, value);
        }
    });
})( jQuery );
(function ( $ ) {

    "use strict";

    $.widget( "metro.rating" , {

        version: "3.0.0",

        options: {
            stars: 5,
            value: 0,
            half: true,
            static: false,
            showScore: true,
            scoreTitle: "Current: ",
            size: 'default',
            colorRate: false,
            onRate: function(v, s, w){return true;},
            onRated: function(v, s, w){}
        },

        _value: 0,

        _create: function () {
            var element = this.element, o = this.options;

            $.each(element.data(), function(key, value){
                if (key in o) {
                    try {
                        o[key] = $.parseJSON(value);
                    } catch (e) {
                        o[key] = value;
                    }
                }
            });

            this._value = parseFloat(o.value);

            this._createRating();
            this._createEvents();
            this._setValue(this._value);
            this._setScore(this._value);

            element.data('rating', this);

        },

        _createRating: function(){
            var element = this.element, o = this.options;
            var i, star, stars, score;

            if (!element.hasClass('rating')) {element.addClass('rating');}
            switch (o.size) {
                case 'small': element.addClass('small'); break;
                case 'large': element.addClass('large'); break;
                default: break;
            }

            if (o.static) {
                element.addClass('static');
            }

            for (i = 0; i < o.stars; i++) {
                star = $("<span/>").addClass('star').appendTo(element).data('star-value', i+1);
            }

            if (o.showScore) {
                score = $("<span/>").addClass('score').appendTo(element);
            }

        },

        _createEvents: function(){
            var that = this, element = this.element, o = this.options;
            var stars;

            stars = element.find('.star');

            stars.on('click', function(e){

                if (typeof o.onRate === 'string') {
                    if (!window[o.onRate]($(this).data('star-value'), this, that)) {
                        return false;
                    }
                } else {
                    if (!o.onRate($(this).data('star-value'), this, that)) {
                        return false;
                    }
                }

                if (typeof o.onRated === 'string') {
                    window[o.onRated]($(this).data('star-value'), this, that);
                } else {
                    o.onRated($(this).data('star-value'), this, that);
                }

                that._value = $(this).data('star-value');
                that._setValue();
                that._setScore();

                e.preventDefault();
                e.stopPropagation();
            });
        },

        _setValue: function(){
            var stars, o = this.options, element = this.element;
            if (o.stars) {
                stars = element.find('.star').removeClass('on half');
                var index = Math.floor(this._value) - 1;
                var half = (this._value - Math.floor(this._value)) * 10 > 0;
                $(stars[index]).addClass('on');
                $(stars[index]).prevAll().addClass('on');
                if (half) {
                    $(stars[index]).next().addClass('on half');
                }
            }

            if (o.colorRate) {
                element.removeClass('poor regular good');
                if (this._value <= 2) {element.addClass('poor');}
                else if (this._value > 2 && this._value <=4) {element.addClass('regular');}
                else if (this._value > 4) {element.addClass('good');}
            }
        },

        _setScore: function(){
            var value = this._value, element = this.element, o = this.options;

            if (value !== undefined) {
                element.find(".score").html(o.scoreTitle + value);
            }
        },

        value: function(value){
            if (value !== undefined) {
                this._setValue();
                this._setScore();
            } else {
                return this._value;
            }
        },

        _destroy: function () {
        },

        _setOption: function ( key, value ) {
            this._super('_setOption', key, value);
        }
    });

})( jQuery );
(function ( $ ) {

    "use strict";

    $.widget( "metro.select" , {

        version: "3.0.0",

        options: {
        },

        _create: function () {
            var that = this, element = this.element, o = this.options;
            var func_options = [
                'templateResult',
                'templateSelection',
                'matcher',
                'initSelection',
                'query'
            ];

            $.each(element.data(), function(key, value){
                try {
                    o[key] = $.parseJSON(value);
                } catch (e) {
                    o[key] = value;
                }
            });

            func_options.map(function(func, index){
                if (o[func] !== undefined) {
                    o[func] = window[o[func]];
                }
            });

            if (o.dropdownParent !== undefined) {
                o.dropdownParent = $(o.dropdownParent);
            }

            if(jQuery().select2) {
                try {
                    element.find("select").select2(o);
                } catch (e) {

                }
            } else {
                alert('Select2 plugin required');
            }

            element.data('select', this);

        },

        _destroy: function () {
        },

        _setOption: function ( key, value ) {
            this._super('_setOption', key, value);
        }
    });

})( jQuery );
(function( $ ) {
    "use strict";

    $.widget("metro.slider", {

        version: "3.0.0",

        options: {
            position: 0,
            accuracy: 0,
            color: 'default',
            completeColor: 'default',
            markerColor: 'default',
            colors: false,
            showHint: false,
            permanentHint: false,
            hintPosition: 'top',
            vertical: false,
			min: 0,
			max: 100,
			animate: true,
            minValue: 0,
            maxValue: 100,
            currValue: 0,
            returnType: 'value',
            target: false,

            onChange: function(value, slider){},
            onChanged: function(value, slider){},

            _slider : {
                vertical: false,
                offset: 0,
                length: 0,
                marker: 0,
                ppp: 0,
                start: 0,
                stop: 0
            }
        },

        _create: function(){
            var that = this,
                element = this.element;


            var o = this.options,
                s = o._slider;

            $.each(element.data(), function(key, value){
                if (key in o) {
                    try {
                        o[key] = $.parseJSON(value);
                    } catch (e) {
                        o[key] = value;
                    }
                }
            });

            o.accuracy = o.accuracy < 0 ? 0 : o.accuracy;
			o.min = o.min < 0 ? 0 : o.min;
			o.min = o.min > o.max ? o.max : o.min;
			o.max = o.max > 100 ? 100 : o.max;
			o.max = o.max < o.min ? o.min : o.max;
            o.position = this._correctValue(element.data('position') > o.min ? (element.data('position') > o.max ? o.max : element.data('position')) : o.min);
            o.colors = o.colors ? o.colors.split(",") : false;

            s.vertical = o.vertical;
            if (o.vertical && !element.hasClass('vertical')) {
                element.addClass('vertical');
            }
            if (o.permanentHint && !element.hasClass('permanent-hint')) {
                element.addClass('permanent-hint');
            }

            if (!o.vertical && o.hintPosition === 'bottom') {
                element.addClass('hint-bottom');
            }

            if (o.vertical && o.hintPosition === 'left') {
                element.addClass('hint-left');
            }

            this._createSlider();
            this._initPoints();
            this._placeMarker(o.position);

            addTouchEvents(element[0]);

            element.children('.marker').on('mousedown', function (e) {
                e.preventDefault();
                that._startMoveMarker(e);
            });

            element.on('mousedown', function (e) {
                e.preventDefault();
                that._startMoveMarker(e);
            });

            element.data('slider', this);

        },

        _startMoveMarker: function(e){
            var element = this.element, o = this.options, that = this, hint = element.children('.slider-hint');
            var returnedValue;

            $(element).on("mousemove", function (event) {
                that._movingMarker(event);
                if (!element.hasClass('permanent-hint')) {
                    hint.css('display', 'block');
                }
            });
            $(element).on("mouseup mouseleave", function () {
                $(element).off('mousemove');
                $(element).off('mouseup');
                element.data('value', o.position);
                element.trigger('changed', o.position);

                returnedValue = o.returnType === 'value' ? that._valueToRealValue(o.position) : o.position;

                if (typeof o.onChanged === 'string') {
                    window[o.onChanged](returnedValue, element);
                } else {
                    o.onChanged(returnedValue, element);
                }


                if (!element.hasClass('permanent-hint')) {
                    hint.css('display', 'none');
                }
            });

            this._initPoints();

            this._movingMarker(e);
        },

        _movingMarker: function (event) {
            var element = this.element, o = this.options;
            var cursorPos,
                percents,
                valuePix,

                vertical = o._slider.vertical,
                sliderOffset = o._slider.offset,
                sliderStart = o._slider.start,
                sliderEnd = o._slider.stop,
                sliderLength = o._slider.length,
                markerSize = o._slider.marker;

            if (vertical) {
                cursorPos = event.pageY - sliderOffset;
            } else {
                cursorPos = event.pageX - sliderOffset;
            }

            if (cursorPos < sliderStart) {
                cursorPos = sliderStart;
            } else if (cursorPos > sliderEnd) {
                cursorPos = sliderEnd;
            }

            if (vertical) {
                valuePix = sliderLength - cursorPos - markerSize / 2;
            } else {
                valuePix = cursorPos - markerSize / 2;
            }

            percents = this._pixToPerc(valuePix);

            this._placeMarker(percents);

            o.currValue = this._valueToRealValue(percents);
            o.position = percents;

            var returnedValue = o.returnType === 'value' ? this._valueToRealValue(o.position) : o.position;

            if (o.target) {
                $(o.target).val(returnedValue);
            }

            if (typeof o.onChange === 'string') {
                window[o.onChange](returnedValue, element);
            } else {
                o.onChange(returnedValue, element);
            }
        },

        _placeMarker: function (value) {
            var size, size2, o = this.options, colorParts, colorIndex = 0, colorDelta, element = this.element,
                marker = this.element.children('.marker'),
                complete = this.element.children('.complete'),
                hint = this.element.children('.slider-hint'), hintValue,
				oldPos = this._percToPix(o.position);

            colorParts = o.colors.length;
            colorDelta = o._slider.length / colorParts;

            if (o._slider.vertical) {
				var oldSize = this._percToPix(o.position) + o._slider.marker,
					oldSize2 = o._slider.length - oldSize;
                size = this._percToPix(value) + o._slider.marker;
                size2 = o._slider.length - size;
                this._animate(marker.css('top', oldSize2),{top: size2});
                this._animate(complete.css('height', oldSize),{height: size});

                if (colorParts) {
                    colorIndex = Math.round(size / colorDelta)-1;
                    complete.css('background-color', o.colors[colorIndex<0?0:colorIndex]);
                }
                if (o.showHint) {
                    hintValue = this._valueToRealValue(value);
                    hint.html(hintValue).css('top', size2 - hint.height()/2 + (element.hasClass('large') ? 8 : 0));
                }
            } else {
                size = this._percToPix(value);
                this._animate(marker.css('left', oldPos),{left: size});
                this._animate(complete.css('width', oldPos),{width: size});
                if (colorParts) {
                    colorIndex = Math.round(size / colorDelta)-1;
                    complete.css('background-color', o.colors[colorIndex<0?0:colorIndex]);
                }
                if (o.showHint) {
                    hintValue = this._valueToRealValue(value);
                    hint.html(hintValue).css({left: size - hint.width() / 2 + (element.hasClass('large') ? 6 : 0)});
                }
            }
        },

        _valueToRealValue: function(value){
            var o = this.options;
            var real_value;

            var percent_value = (o.maxValue - o.minValue) / 100;

            real_value = value * percent_value + o.minValue;

            return Math.round(real_value);
        },
		
		_animate: function (obj, val) {
            var o = this.options;

			if(o.animate) {
				obj.stop(true).animate(val);
			} else {
				obj.css(val);
			}
		},

        _pixToPerc: function (valuePix) {
            var valuePerc;
            valuePerc = valuePix * this.options._slider.ppp;
            return Math.round(this._correctValue(valuePerc));
        },

        _percToPix: function (value) {
            if (this.options._slider.ppp === 0) {
                return 0;
            }
            return Math.round(value / this.options._slider.ppp);
        },

        _correctValue: function (value) {
            var o = this.options;
            var accuracy = o.accuracy;
			var max = o.max;
			var min = o.min;
            if (accuracy === 0) {
                return value;
            }
            if (value === max) {
                return max;
            }
			if (value === min) {
                return min;
            }
            value = Math.floor(value / accuracy) * accuracy + Math.round(value % accuracy / accuracy) * accuracy;
            if (value > max) {
                return max;
            }
			if (value < min) {
                return min;
            }
            return value;
        },

        _initPoints: function(){
            var o = this.options, s = o._slider, element = this.element;

            if (s.vertical) {
                s.offset = element.offset().top;
                s.length = element.height();
                s.marker = element.children('.marker').height();
            } else {
                s.offset = element.offset().left;
                s.length = element.width();
                s.marker = element.children('.marker').width();
            }

            s.ppp = o.max / (s.length - s.marker);
            s.start = s.marker / 2;
            s.stop = s.length - s.marker / 2;
        },

        _createSlider: function(){
            var element = this.element,
                o = this.options,
                complete, marker, hint;

            element.html('');

            complete = $("<div/>").addClass("complete").appendTo(element);
            marker = $("<a/>").addClass("marker").appendTo(element);

            if (o.showHint) {
                hint = $("<span/>").addClass("slider-hint").appendTo(element);
            }

            if (o.color !== 'default') {
                if (o.color.isColor()) {
                    element.css('background-color', o.color);
                } else {
                    element.addClass(o.color);
                }
            }
            if (o.completeColor !== 'default') {
                if (o.completeColor.isColor()) {
                    complete.css('background-color', o.completeColor);
                } else {
                    complete.addClass(o.completeColor);
                }
            }
            if (o.markerColor !== 'default') {
                if (o.markerColor.isColor()) {
                    marker.css('background-color', o.markerColor);
                } else {
                    marker.addClass(o.markerColor);
                }
            }
        },

        value: function (value) {
            var o = this.options, returnedValue;

            if (typeof value !== 'undefined') {

                value = value > o.max ? o.max : value;
                value = value < o.min ? o.min : value;

                this._placeMarker(parseInt(value));
                o.position = parseInt(value);

                returnedValue = o.returnType === 'value' ? this._valueToRealValue(o.position) : o.position;


                if (typeof o.onChange === 'string') {
                    window[o.onChange](returnedValue, this.element);
                } else {
                    o.onChange(returnedValue, this.element);
                }

                return this;
            } else {
                returnedValue = o.returnType === 'value' ? this._valueToRealValue(o.position) : o.position;
                return returnedValue;
            }
        },

        _destroy: function(){},

        _setOption: function(key, value){
            this._super('_setOption', key, value);
        }
    });
})( jQuery );
(function( $ ) {

    "use strict";

    $.widget("metro.stepper", {

        version: "3.0.0",

        options: {
            steps: 3,
            start: 1,
            type: 'default',
            clickable: true,
            onStep: function(index, step){},
            onStepClick: function(index, step){}
        },

        _create: function(){
            var element = this.element, o = this.options, element_id = element.attr('id');

            $.each(element.data(), function(key, value){
                if (key in o) {
                    try {
                        o[key] = $.parseJSON(value);
                    } catch (e) {
                        o[key] = value;
                    }
                }
            });

            if (!element.hasClass('stepper')) {element.addClass('stepper');}
            if (element_id === undefined) {
                element_id = window.uniqueId(this.widgetName+'_');
                element.attr('id', element_id);
            }

            this._createStepper();
            if (o.clickable) {this._createEvents();}
            this._positioningSteps();
            this._stepTo(o.start);

            element.data('stepper', this);

        },

        _createEvents: function(){
            var that = this, element = this.element, o= this.options;
            element.on('click', 'li', function(e){
                var step = $(this).data('step');


                if (typeof o.onStepClick === 'string') {
                    window[o.onStepClick](step - 1, step);
                } else {
                    o.onStepClick(step - 1, step);
                }

                element.trigger("stepclick", step);
            });
        },

        _createStepper: function(){
            var element = this.element, o= this.options;
            var i, ul, li;

            ul = $("<ul/>");

            switch(o.type) {
                case 'diamond': element.addClass('diamond'); break;
                case 'cycle': element.addClass('cycle'); break;
            }

            for(i=0;i< o.steps;i++) {
                li = $("<li/>").data('step', i + 1).appendTo(ul);
            }
            ul.appendTo(element);
        },

        _positioningSteps: function(){
            var that = this, element = this.element, o = this.options,
                steps = element.find("li"),
                element_width = element.width(),
                steps_length = steps.length-1,
                step_width = $(steps[0]).width();

            $.each(steps, function(i, step){
                var left = i === 0 ? 0 : (element_width - step_width)/steps_length * i;
                $(step).animate({
                    left: left
                });
            });
        },

        _stepTo: function(step){
            var element = this.element, o = this.options;
            var steps = element.find("li");

            steps.removeClass('current').removeClass('complete');

            $.each(steps, function(i, s){
                if (i < step - 1) {$(s).addClass('complete');}
                if (i === step - 1) {
                    $(s).addClass('current') ;

                    if (typeof  o.onStep === 'string') {
                        window[o.onStep](i+1, s);
                    } else {
                        o.onStep(i+1, s);
                    }
                }
            });
        },

        stepTo: function(step){
            this._stepTo(step);
        },

        first: function(){
            var o = this.options;
            o.start = 1;
            this._stepTo(o.start);
        },

        last: function(){
            var element = this.element, o = this.options;
            var steps = element.find("li");

            o.start = steps.length;
            this._stepTo(o.start);
        },

        next: function(){
            var element = this.element, o = this.options;
            var steps = element.find("li");

            if (o.start + 1 > steps.length) {return;}

            o.start++;
            this._stepTo(o.start);
        },

        prior: function(){
            var o = this.options;

            if (o.start - 1 === 0) {return;}

            o.start--;
            this._stepTo(o.start);
        },

        _destroy: function(){
        },

        _setOption: function(key, value){
            this._super('_setOption', key, value);
        }
    });
})( jQuery );


(function( $ ) {
    $.widget("metro.streamer", {

        version: "3.0.0",

        options: {
            scrollBar: false,
            meterStart: 9,
            meterStop: 19,
            meterInterval: 20,
            slideToTime: "default",
            slideSleep: 1000,
            slideSpeed: 1000,
            onClick: function(event){},
            onLongClick: function(event){}
        },

        _create: function(){
            var that = this, element = this.element, o = this.options,
                streams = element.find(".stream"),
                events = element.find(".event"),
                events_container = element.find(".events"),
                events_area = element.find(".events-area"),
                groups = element.find(".event-group"),
                event_streams = element.find(".event-stream");


            $.each(element.data(), function(key, value){
                if (key in o) {
                    try {
                        o[key] = $.parseJSON(value);
                    } catch (e) {
                        o[key] = value;
                    }
                }
            });

            element.data('streamSelect', -1);

            var meter = $("<ul/>").addClass("meter");
            var i, j, m, start = o.meterStart, stop = o.meterStop, interval = o.meterInterval;

            var _intervals = [];
            for (i = start; i<stop; i++) {
                for (j = 0; j < 60; j+=interval) {
                    m = (i<10?"0"+i:i)+":"+(j<10?"0"+j:j);
                    $("<li/>").addClass("js-interval-"+ m.replace(":", "-")).html("<em>"+m+"</em>").appendTo(meter);
                    _intervals.push(m);
                }
            }
            element.data("intervals", _intervals);
            meter.insertBefore(element.find(".events-grid"));

            //console.log(element.data("intervals"));

            // Re-Calc all event-stream width and set background for time
            element.find(".event-stream").each(function(i, s){
                var event_stream_width = 0;
                var events = $(s).find(".event");

                events.each(function(i, el){
                    event_stream_width += $(el).outerWidth() + parseInt($(el).css('margin-left'));
                });

                $(s).css({
                    width: (event_stream_width + ( (events.length-1) * 2 ) + 1)
                });

                $(s).find(".time").css("background-color", $(streams[i]).css('background-color'));
            });

            // Set scrollbar
            events_container.css({
                'overflow-x': (o.scrollBar ? 'scroll' : 'hidden')
            });

            // Set streamer height
            element.css({
                height: element.find(".streams").outerHeight() + (o.scrollBar ? 20 : 0)
            });

            // Re-Calc events-area width
            var events_area_width = 0;
            groups.each(function(i, el){
                events_area_width += $(el).outerWidth();
            });
            events_area_width += ( (groups.length-1) * 2 ) + 10;
            events_area.css('width', events_area_width);

            events.each(function(i, el){
                addTouchEvents(el);
            });

            element.mousewheel(function(event, delta){
                var scroll_value = delta * 50;
                events_container.scrollLeft(events_container.scrollLeft() - scroll_value);
                return false;
            });

            streams.each(function(i, s){
                $(s).mousedown(function(e){
                    if (element.data('streamSelect') == i) {
                        events.removeClass('event-disable');
                        element.data('streamSelect', -1);
                    } else {
                        element.data('streamSelect', i);
                        events.addClass('event-disable');
                        $(event_streams[i]).find(".event").removeClass("event-disable");
                    }
                });
            });

            this._createEvents();

            this.slideToTime(o.slideToTime, o.slideSleep, o.slideSpeed);

            element.data('streamer', this);
        },

        _createEvents: function(){
            var that = this, element = this.element, o = this.options;
            var events = element.find(".event");

            events.on('click', function(e){
                if (e.ctrlKey) {
                    $(this).toggleClass("selected");
                }
                e.preventDefault();
                o.onClick($(this));
            });

            element.find(".js-go-previous-time").on('click', function(e){
                var next_index = element.data("intervals").indexOf(element.data("current-time"));
                if (next_index == 0) {
                    return;
                }
                next_index--;
                var new_time = element.data("intervals")[next_index];
                that.slideToTime(new_time, 0, o.slideSpeed);
            });

            element.find(".js-go-next-time").on('click', function(e){
                var next_index = element.data("intervals").indexOf(element.data("current-time"));
                if (next_index == element.data("intervals").length - 1) {
                    return;
                }
                next_index++;
                var new_time = element.data("intervals")[next_index];
                that.slideToTime(new_time, 0, o.slideSpeed);
            });

            element.find(".js-show-all-streams").on("click", function(e){
                element.find(".event").removeClass("event-disable");
                element.data('streamSelect', -1);
                e.preventDefault();
            });


            element.find(".js-schedule-mode").on("click", function(e){
                $(this).toggleClass("active");
                element.data("schedule-mode", $(this).hasClass("inverse"));
                e.preventDefault();
            });
        },

        slideToTime: function(time, sleep, speed){
            var that = this, element = this.element,
                interval, _time;

            if (time === 'default') {
                interval = element.find(".meter li")[0];
                time = interval.className.replace("js-interval-", "").replace("-", ":");
            } else {
                _time = time.split(":");

                if (_time[0].length === 1) {
                    time = '0' + time;
                }

            }

            interval = element.find(".meter li.js-interval-"+time.replace(":", "-"))[0];

            setTimeout(function(){
                element.find(".events").animate({
                    scrollLeft:  (interval.offsetLeft)
                }, speed, function(){
                    that._afterSlide();
                });
            }, sleep);

            element.data("current-time", time);
        },

        _afterSlide: function(){

        },

        _destroy: function(){

        },

        _setOption: function(key, value){
            this._super('_setOption', key, value);
        }
    })
})( jQuery );



(function ( $ ) {
    "use strict";

    $.widget( "metro.tabControl" , {

        version: "3.0.0",

        options: {
            openTarget: false,
            saveState: false,
            onTabClick: function(tab){return true;},
            onTabChanged: function(tab){},
            _current: {tab: false, frame: false}
        },


        _create: function () {
            var that = this, element = this.element, o = this.options;
            var tabs = element.children('.tabs').find('li').children('a');
            var frames = element.children('.frames').children('div');
            var tab, target, frame;

            $.each(element.data(), function(key, value){
                if (key in o) {
                    try {
                        o[key] = $.parseJSON(value);
                    } catch (e) {
                        o[key] = value;
                    }
                }
            });

            if (o.saveState && element.attr('id') !== undefined && element.attr('id').trim() !== '') {

                var stored_target = window.localStorage.getItem(element.attr('id')+"-stored-tab");
                if (stored_target && stored_target !== 'undefined') {
                    tab = element.find("a[href='"+stored_target+"']");
                    if (tab) {
                        target = tab.attr('href');
                        frame = target && target.isUrl() ? false : $(target);
                        o._current.tab = tab;
                        o._current.frame = frame;
                    }
                }
            }

            if (!o._current.tab && o.openTarget !== false) {

                tab = element.find("a[href='"+ o.openTarget+"']");
                if (tab) {
                    target = tab.attr('href');
                    frame = target && target.isUrl() ? false : $(target);
                    o._current.tab = tab;
                    o._current.frame = frame;
                }
            }

            if (!o._current.tab) {

                $.each(tabs, function (i, v) {
                    var tab = $(v), target = tab.attr('href'), frame = target.isUrl() ? false : $(target);
                    if (tab.parent().hasClass('active') && !tab.parent().hasClass('disabled') && frame !== false) {
                        o._current.tab = tab;
                        o._current.frame = frame;
                    }
                });
            }

            if (!o._current.tab) {

                for(var i = 0; i < tabs.length; i++) {
                    if (!$(tabs[i]).attr('href').isUrl() && !$(tabs[i]).parent().hasClass('disabled')) {
                        o._current.tab = $(tabs[i]);
                        o._current.frame = $($(tabs[i]).attr('href'));
                        break;
                    }
                }
            }

            this._createEvents();
            this._openTab();

            //this._hideTabs();
            //
            //$(window).on('resize', function(){
            //    that._hideTabs();
            //});

            element.data('tabControl', this);

        },

        _hideTabs: function(){
            var element = this.element;
            var w = element.outerWidth();
            var _tabs = element.children('.tabs').find('li:not(.non-visible-tabs)');
            var _nvt = element.children('.tabs').find('.non-visible-tabs').children('.d-menu');

            $.each(_tabs, function(){
                var $tab = $(this), tab = this;
                if (tab.offsetLeft + tab.offsetWidth + 30 > w) {
                    var new_tab = $tab.clone(true);
                    new_tab.appendTo(_nvt);
                    $tab.remove();
                }
            });
        },

        _openTab: function(){
            var element = this.element, o = this.options;
            var tabs = element.children('.tabs').find('li').children('a');
            var frames = element.children('.frames').children('div');

            tabs.parent().removeClass('active');
            frames.hide();

            o._current.tab.parent().addClass('active');
            o._current.frame.show();

            if (o.saveState && element.attr('id') !== undefined && element.attr('id').trim() !== '') {
                window.localStorage.setItem(element.attr('id')+"-stored-tab", o._current.tab.attr('href'));
            }
        },

        _createEvents: function(){
            var that = this, element = this.element, o = this.options;
            var tabs = element.children('.tabs').find('li').children('a');
            var frames = element.children('.frames').children('div');

            element.on('click', '.tabs > li > a', function(e){
                var tab = $(this), target = tab.attr('href'), frame = $(target);

                if (tab.parent().hasClass('disabled')) {return false;}

                if (typeof o.onTabClick === 'string') {
                    if (!window[o.onTabClick](tab)) {return false;}
                } else {
                    if (!o.onTabClick(tab)) {return false;}
                }

                if (target.isUrl()) {
                    window.location.href = target;
                    return true;
                }

                o._current.tab = tab;
                o._current.frame = frame;

                that._openTab();

                if (typeof o.onTabChanged === 'string') {
                    window[o.onTabChanged](tab);
                } else {
                    o.onTabChanged(tab);
                }

                e.preventDefault();
                e.stopPropagation();
            });
        },

        hideTab: function(tab){

        },

        showTab: function(tab){

        },

        _destroy: function () {
        },

        _setOption: function ( key, value ) {
            this._super('_setOption', key, value);
        }
    });

})( jQuery );
(function ( $ ) {

    "use strict";

    $.widget( "metro.tile" , {

        version: "3.0.0",

        options: {
            effect: 'slideLeft',
            period: 4000,
            duration: 700,
            easing: 'doubleSqrt'
        },

        _frames: {},
        _currentIndex: 0,
        _interval: 0,
        _outPosition: 0,
        _size: {},

        _create: function () {
            var that = this, element = this.element, o = this.options;

            $.each(element.data(), function(key, value){
                if (key in o) {
                    try {
                        o[key] = $.parseJSON(value);
                    } catch (e) {
                        o[key] = value;
                    }
                }
            });

            this._createTransformTile();
            this._createLiveTile();

            element.data('tile', this);

        },

        _createLiveTile: function(){
            var that = this, element = this.element, o = this.options;

            this._frames = element.find(".live-slide");
            if (this._frames.length <= 1) {return false;}

            $.easing.doubleSqrt = function(t) {return Math.sqrt(Math.sqrt(t));};

            this._size = {
                'width': element.width(),
                'height': element.height()
            };

            element.on('mouseenter', function(){
                that.stop();
            });

            element.on('mouseleave', function(){
                that.start();
            });

            this.start();
        },

        start: function(){
            var that = this;
            this._interval = setInterval(function(){
                that._animate();
            }, this.options.period);
        },

        stop: function(){
            clearInterval(this._interval);
        },

        _animate: function(){
            var currentFrame = this._frames[this._currentIndex], nextFrame;
            this._currentIndex += 1;
            if (this._currentIndex >= this._frames.length) {this._currentIndex = 0;}
            nextFrame = this._frames[this._currentIndex];

            switch (this.options.effect) {
                case 'slideLeft': this._effectSlideLeft(currentFrame, nextFrame); break;
                case 'slideRight': this._effectSlideRight(currentFrame, nextFrame); break;
                case 'slideDown': this._effectSlideDown(currentFrame, nextFrame); break;
                case 'slideUpDown': this._effectSlideUpDown(currentFrame, nextFrame); break;
                case 'slideLeftRight': this._effectSlideLeftRight(currentFrame, nextFrame); break;
                default: this._effectSlideUp(currentFrame, nextFrame);
            }
        },

        _effectSlideLeftRight: function(currentFrame, nextFrame){
            if (this._currentIndex % 2 === 0) {
                this._effectSlideLeft(currentFrame, nextFrame);
            } else {
                this._effectSlideRight(currentFrame, nextFrame);
            }
        },

        _effectSlideUpDown: function(currentFrame, nextFrame){
            if (this._currentIndex % 2 === 0) {
                this._effectSlideUp(currentFrame, nextFrame);
            } else {
                this._effectSlideDown(currentFrame, nextFrame);
            }
        },

        _effectSlideUp: function(currentFrame, nextFrame){
            var _out = this._size.height;
            var options = {
                'duration': this.options.duration,
                'easing': this.options.easing
            };

            $(currentFrame)
                .animate({top: -_out}, options);
            $(nextFrame)
                .css({top: _out})
                .show()
                .animate({top: 0}, options);
        },

        _effectSlideDown: function(currentFrame, nextFrame){
            var _out = this._size.height;
            var options = {
                'duration': this.options.duration,
                'easing': this.options.easing
            };

            $(currentFrame)
                .animate({top: _out}, options);
            $(nextFrame)
                .css({top: -_out})
                .show()
                .animate({top: 0}, options);
        },

        _effectSlideLeft: function(currentFrame, nextFrame){
            var _out = this._size.width;
            var options = {
                'duration': this.options.duration,
                'easing': this.options.easing
            };

            $(currentFrame)
                .animate({left: _out * -1}, options);
            $(nextFrame)
                .css({left: _out})
                .show()
                .animate({left: 0}, options);
        },

        _effectSlideRight: function(currentFrame, nextFrame){
            var _out = this._size.width;
            var options = {
                'duration': this.options.duration,
                'easing': this.options.easing
            };

            $(currentFrame)
                .animate({left: _out}, options);
            $(nextFrame)
                .css({left: -_out})
                .show()
                .animate({left: 0}, options);
        },

        _createTransformTile: function(){
            var that = this, element = this.element, o = this.options;
            var dim = {w: element.width(), h: element.height()};

            element.on('mousedown', function(e){
                var X = e.pageX - $(this).offset().left, Y = e.pageY - $(this).offset().top;
                var transform = 'top';

                if (X < dim.w * 1/3 && (Y < dim.h * 1/2 || Y > dim.h * 1/2 )) {
                    transform = 'left';
                } else if (X > dim.w * 2/3 && (Y < dim.h * 1/2 || Y > dim.h * 1/2 )) {
                    transform = 'right';
                } else if (X > dim.w*1/3 && X<dim.w*2/3 && Y > dim.h/2) {
                    transform = 'bottom';
                }

                $(this).addClass("tile-transform-"+transform);

                console.log(transform);

                if (element[0].tagName === 'A' && element.attr('href')) {
                    setTimeout(function(){
                        document.location.href = element.attr('href');
                    }, 500);
                }
            });

            element.on('mouseup', function(){
                $(this)
                    .removeClass("tile-transform-left")
                    .removeClass("tile-transform-right")
                    .removeClass("tile-transform-top")
                    .removeClass("tile-transform-bottom");
            });
            element.on('mouseleave', function(){
                $(this)
                    .removeClass("tile-transform-left")
                    .removeClass("tile-transform-right")
                    .removeClass("tile-transform-top")
                    .removeClass("tile-transform-bottom");
            });
        },

        _destroy: function () {
        },

        _setOption: function ( key, value ) {
            this._super('_setOption', key, value);
        }
    });

})( jQuery );
(function ( $ ) {

    "use strict";

    $.widget( "metro.treeview" , {

        version: "3.0.0",

        options: {
            doubleClick: true,
            onClick: function(leaf, node, pnode, tree){},
            onExpand: function(leaf, node, pnode, tree){},
            onCollapse: function(leaf, node, pnode, tree){}
        },

        _create: function () {
            var that = this, element = this.element, o = this.options;

            $.each(element.data(), function(key, value){
                if (key in o) {
                    try {
                        o[key] = $.parseJSON(value);
                    } catch (e) {
                        o[key] = value;
                    }
                }
            });

            this._initTree();
            this._createEvents();

            element.data('treeview', this);
        },


        _createCheckbox: function(leaf, parent){
            var input, checkbox, check;

            input = $("<label/>").addClass("input-control checkbox small-check").insertBefore(leaf);
            checkbox = $("<input/>").attr('type', 'checkbox').appendTo(input);
            check = $("<span/>").addClass('check').appendTo(input);
            if (parent.data('name') !== undefined) {
                checkbox.attr('name', parent.data('name'));
            }
            if (parent.data('id') !== undefined) {
                checkbox.attr('id', parent.data('id'));
            }
            if (parent.data('checked') !== undefined) {
                checkbox.prop('checked', parent.data('checked'));
            }
            if (parent.data('readonly') !== undefined) {
                checkbox.prop('disabled', parent.data('readonly'));
            }
            if (parent.data('disabled') !== undefined) {
                checkbox.prop('disabled', parent.data('disabled'));
                if (parent.data('disabled') === true) {
                    parent.addClass('disabled');
                }
            }
        },

        _createRadio: function(leaf, parent){
            var input, checkbox, check;

            input = $("<label/>").addClass("input-control radio small-check").insertBefore(leaf);
            checkbox = $("<input/>").attr('type', 'radio').appendTo(input);
            check = $("<span/>").addClass('check').appendTo(input);
            if (parent.data('name') !== undefined) {
                checkbox.attr('name', parent.data('name'));
            }
            if (parent.data('id') !== undefined) {
                checkbox.attr('id', parent.data('id'));
            }
            if (parent.data('checked') !== undefined) {
                checkbox.prop('checked', parent.data('checked'));
            }
            if (parent.data('readonly') !== undefined) {
                checkbox.prop('disabled', parent.data('readonly'));
            }
            if (parent.data('disabled') !== undefined) {
                checkbox.prop('disabled', parent.data('disabled'));
                if (parent.data('disabled') === true) {
                    parent.addClass('disabled');
                }
            }
        },

        _initTree: function(){
            var that = this, element = this.element, o = this.options;
            var leafs = element.find('.leaf');
            $.each(leafs, function(){
                var leaf = $(this), parent = leaf.parent('li'), ul = leaf.siblings('ul'), node = $(leaf.parents('.node')[0]);
                //var input, checkbox, check;

                if (parent.data('mode') === 'checkbox') {
                    that._createCheckbox(leaf, parent);
                }

                if (parent.data('mode') === 'radio') {
                    that._createRadio(leaf, parent);
                }

                if (ul.length > 0) {
                    if (!parent.hasClass('node')) {
                        parent.addClass('node');
                    }
                }
                if (parent.hasClass('collapsed')) {
                    ul.hide();
                }
            });
        },

        _renderChecks: function(check){
            var element = this.element, that = this, o = this.options;
            var state = check.is(":checked");
            var parent = $(check.parent().parent());
            var children_checks = parent.children('ul').find('[type="checkbox"]');

            children_checks.prop('checked', state).removeClass('indeterminate');

            $.each(element.find('.node[data-mode=checkbox]').reverse(), function(){
                var node = $(this),
                    ch = node.children('.input-control').find('[type="checkbox"]'),
                    children_all = node.children('ul').find('[type="checkbox"]'),
                    children_checked = node.children('ul').find('[type="checkbox"]:checked');

                ch.removeClass('indeterminate');
                if (children_checked.length === 0) {
                    ch.prop("checked", false);
                    ch.removeClass('indeterminate');
                } else
                if (children_checked.length > 0 && children_all.length > children_checked.length) {
                    ch.prop('checked', true);
                    ch.addClass('indeterminate');
                }
            });

        },

        _createEvents: function(){
            var that = this, element = this.element, o = this.options;

            element.on('change', 'input:checkbox', function(){
                that._renderChecks($(this));
            });

            element.on('click', 'input', function(){
                var leaf = $(this),
                    node = $(leaf.parents('.node')[0]),
                    parent = leaf.parent('li'),
                    check = leaf.siblings('.input-control').find('input:checkbox'),
                    radio = leaf.siblings('.input-control').find('input:radio'),
                    new_check_state,
                    check_disabled;

                if (check.length > 0) {
                    new_check_state = !check.is(":checked");
                    check_disabled = check.is(":disabled");
                    if (!check_disabled) {check.prop('checked', new_check_state);}
                    that._renderChecks(check);
                }
                if (radio.length > 0) {
                    check_disabled = radio.is(":disabled");
                    if (!check_disabled) {radio.prop('checked', true);}
                }

                if (typeof o.onClick === 'string') {
                    window[o.onClick](leaf, parent, node, that);
                } else {
                    o.onClick(leaf, parent, node, that);
                }
            });

            element.on('click', '.leaf', function(){
                var leaf = $(this),
                    node = $(leaf.parents('.node')[0]),
                    parent = leaf.parent('li');

                element.find('.leaf').parent('li').removeClass('active');
                parent.addClass('active');

                if (typeof o.onClick === 'string') {
                    window[o.onClick](leaf, parent, node, that);
                } else {
                    o.onClick(leaf, parent, node, that);
                }
            });

            if (o.doubleClick) {
                element.on('dblclick', '.leaf', function (e) {
                    var leaf = $(this), parent = leaf.parent('li'), node = $(leaf.parents('.node')[0]);

                    if (parent.hasClass("keep-open")) {
                        return false;
                    }

                    parent.toggleClass('collapsed');
                    if (!parent.hasClass('collapsed')) {
                        parent.children('ul').slideDown('fast');
                        if (typeof o.onExpand === 'string') {
                            window[o.onExpand](parent, leaf, node);
                        } else {
                            o.onExpand(parent, leaf, node);
                        }
                    } else {
                        parent.children('ul').slideUp('fast');
                        if (typeof o.onCollapse === 'string') {
                            window[o.onCollapse](leaf, parent, node, that);
                        } else {
                            o.onCollapse(leaf, parent, node, that);
                        }
                    }
                    e.stopPropagation();
                    e.preventDefault();
                });
            }

            element.on('click', '.node-toggle', function(e){
                var leaf = $(this).siblings('.leaf'), parent = $(this).parent('li'), node = $(leaf.parents('.node')[0]);

                if (parent.hasClass("keep-open")) {return false;}

                parent.toggleClass('collapsed');
                if (!parent.hasClass('collapsed')) {
                    parent.children('ul').slideDown('fast');
                    if (typeof o.onExpand === 'string') {
                        window[o.onExpand](leaf, parent, node, that);
                    } else {
                        o.onExpand(leaf, parent, node, that);
                    }
                } else {
                    parent.children('ul').slideUp('fast');
                    if (typeof o.onCollapse === 'string') {
                        window[o.onCollapse](leaf, parent, node, that);
                    } else {
                        o.onCollapse(leaf, parent, node, that);
                    }
                }
                e.stopPropagation();
                e.preventDefault();
            });
        },

        addLeaf: function(parent, name, data){
            var element = this.element;
            var leaf, li, ul;

            if (parent) {
                if (parent[0].tagName === "LI") {parent.addClass('node');}
                if (parent.children('.node-toggle').length === 0) {
                    $("<span/>").addClass('node-toggle').appendTo(parent);
                }
            }

            ul = parent ? $(parent).children('ul') : element.children('ul');

            if (ul.length === 0) {
                ul = $("<ul/>").appendTo(parent ? parent : element);
            }

            li = $("<li/>").appendTo( ul );

            if (data !== undefined) {
                if (data.tagName !== undefined) {
                    leaf = $("<"+data.tagName+"/>").addClass("leaf").appendTo(li);
                } else {
                    leaf = $("<span/>").addClass("leaf").appendTo(li);
                }
            } else {
                leaf = $("<span/>").addClass("leaf").appendTo(li);
            }

            leaf.html(name);

            if (data !== undefined) {
                $.each(data, function(key, value){
                    li.attr("data-"+key, value);
                });
                if (data.mode !== undefined) {
                    switch (data.mode) {
                        case 'checkbox' : this._createCheckbox(leaf, li); break;
                        case 'radio' : this._createRadio(leaf, li); break;
                    }
                }
            }

            return this;
        },

        _destroy: function () {
        },

        _setOption: function ( key, value ) {
            this._super('_setOption', key, value);
        }
    });

})( jQuery );
(function ( $ ) {

    "use strict";

    $.widget( "metro.window" , {

        version: "3.0.0",

        options: {
            parent: 'default',
            captionStyle: false,
            contentStyle: false,
            buttons: {
                btnMin: false,
                btnMax: false,
                btnClose: false
            },
            title: false,
            content: false,
            icon: false,
            type: 'default', // 'modal'
            size: false, // {width: x, height: y}

            onBtnMinClick: function(e){e.preventDefault();},
            onBtnMaxClick: function(e){e.preventDefault();},
            onBtnCloseClick: function(e){e.preventDefault();},
            onShow: function(e){e.preventDefault();},
            onHide: function(e){e.preventDefault();}
        },

        _create: function () {
            var element = this.element, o = this.options;

            $.each(element.data(), function(key, value){
                if (key in o) {
                    try {
                        o[key] = $.parseJSON(value);
                    } catch (e) {
                        o[key] = value;
                    }
                }
            });

            this._createWindow();

            element.data('window', this);

        },

        _createWindow: function(){
            var that = this, element = this.element, o = this.options;
            var wind = element, capt, cont;

            wind.addClass("window");
            capt = $("<div/>").addClass("window-caption");
            cont = $("<div/>").addClass("window-content");

            if (o.icon || o.title) {capt.appendTo(wind);}
            cont.appendTo(wind);

            if (typeof o.size === 'object') {
                $.each(o.size, function(key, value){
                    cont.css(key, value);
                });
            }

            if (o.captionStyle && typeof o.captionStyle === 'object') {
                $.each(o.captionStyle, function(key, value){
                    if (value.isColor()) {
                        capt.css(key, value + " !important");
                    } else {
                        capt.addClass(value);
                    }
                });
            }

            if (o.contentStyle && typeof o.contentStyle === 'object') {
                $.each(o.contentStyle, function(key, value){
                    if (value.isColor()) {
                        cont.css(key, value + " !important");
                    } else {
                        cont.addClass(value);
                    }
                });
            }

            wind.appendTo(o.parent !== 'default' ? o.parent : element.parent());

            this.icon();
            this.title();
            this.buttons();
            this.content();
        },

        icon: function(){
            var o = this.options;
            var capt = this.element.children('.window-caption');
            var icon = capt.find(".window-caption-icon");

            if (o.icon) {
                if (icon.length === 0) {
                    $("<span/>").addClass('window-caption-icon').html(o.icon).appendTo(capt);
                } else {
                    icon.html(o.icon);
                }

            }
        },

        title: function(){
            var o = this.options;
            var capt = this.element.children('.window-caption');
            var title = capt.find(".window-caption-title");

            if (o.title) {
                if (title.length === 0) {
                    $("<span/>").addClass('window-caption-title').html(o.title).appendTo(capt);
                } else {
                    title.html(o.title);
                }
            }
        },

        buttons: function(){
            var o = this.options;
            var bMin, bMax, bClose;
            var capt = this.element.children('.window-caption');

            if (capt.length === 0) {return;}

            if (o.buttons) {
                var btnMin = o.buttons.btnMin;
                var btnMax = o.buttons.btnMax;
                var btnClose = o.buttons.btnClose;

                if (btnMin && btnMin !== false) {
                    bMin = $("<span/>").addClass('btn-min').appendTo(capt);
                    if (typeof btnMin === 'object') {
                        bMin.css(btnMin);
                    }
                    if (typeof o.onBtnMinClick === 'string') {
                        var bMinFn = window[o.onBtnMinClick];
                        bMin.on('click', bMinFn);
                    } else {
                        bMin.on('click', o.onBtnMinClick(e));
                    }
                }

                if (btnMax && btnMax !== false) {
                    bMax = $("<span/>").addClass('btn-max').appendTo(capt);
                    if (typeof btnMax === 'object') {
                        bMax.css(btnMax);
                    }
                    if (typeof o.onBtnMaxClick === 'string') {
                        var bMaxFn = window[o.onBtnMaxClick];
                        bMax.on('click', bMaxFn);
                    } else {
                        bMax.on('click', o.onBtnMaxClick(e));
                    }
                }

                if (btnClose && btnClose !== false) {
                    bClose = $("<span/>").addClass('btn-close').appendTo(capt);
                    if (typeof btnClose === 'object') {
                        bClose.css(btnClose);
                    }
                    if (typeof o.onBtnCloseClick === 'string') {
                        var bCloseFn = window[o.onBtnCloseClick];
                        bClose.on('click', bCloseFn);
                    } else {
                        bClose.on('click', o.onBtnCloseClick(e));
                    }
                }
            }
        },

        content: function(){
            var o = this.options;
            var c = o.content;
            var content = this.element.children('.window-content');

            if (!c) {return;}

            if (c.isUrl()) {
                if (c.indexOf('youtube') > -1) {
                    var iframe = $("<iframe>");
                    var video_container = $("<div/>").addClass('video-container').appendTo(content);

                    iframe
                        .attr('src', c)
                        .attr('frameborder', '0');

                    iframe.appendTo(video_container);
                }
            } else {
                content.html(c);
            }
        },

        _destroy: function () {
        },

        _setOption: function ( key, value ) {
            this._super('_setOption', key, value);
        }
    });
})( jQuery );
(function( $ ) {

    "use strict";

    $.widget("metro.wizard", {

        version: "3.0.0",

        options: {
            stepper: true,
            stepperType: 'default',
            stepperClickable: false,
            startPage: 'default',
            finishStep: 'default',
            locale: window.METRO_CURRENT_LOCALE,
            buttons: {
                cancel: true,
                help: true,
                prior: true,
                next: true,
                finish: true
            },

            onCancel: function(page, wiz){},
            onHelp: function(page, wiz){},
            onPrior: function(page, wiz){return true;},
            onNext: function(page, wiz){return true;},
            onFinish: function(page, wiz){},

            onPage: function(page, wiz){},
            onStepClick: function(step){}
        },

        _stepper: undefined,
        _currentStep: 0,
        _steps: undefined,

        _create: function(){
            var that = this,
                element = this.element,
                o = this.options,
                steps = element.find(".step");

            $.each(element.data(), function(key, value){
                if (key in o) {
                    try {
                        o[key] = $.parseJSON(value);
                    } catch (e) {
                        o[key] = value;
                    }
                }
            });

            this._steps = steps;

            if (o.stepper) {
                this._stepper = this._createStepper(steps.length)
                    .insertBefore(element.find('.steps'))
                    .stepper({
                        clickable: o.stepperClickable
                    }).on('stepclick', function(e, s){
                        that.stepTo(s);
                        if (typeof o.onStepClick === 'string' ) {
                            window[o.onStepClick](s);
                        } else {
                            o.onStepClick(s);
                        }
                    });
            }

            if (element.data('locale') !== undefined) {o.locale = element.data('locale');}

            this._createEvents();

            var sp = (o.startPage !== 'default' && parseInt(o.startPage) > 1) ? o.startPage : 1;
            this.stepTo(sp);

            if (typeof o.onPage === 'string') {
                window[o.onPage](this._currentStep + 1, element);
            } else {
                o.onPage(this._currentStep + 1, element);
            }

            element.data('wizard', this);

        },

        _createStepper: function(steps){
            var stepper, o = this.options;

            stepper = $("<div/>").addClass("stepper")
                .attr("data-role", "stepper")
                .attr("data-steps", steps);

            if (o.stepperType !== 'default') {
                stepper.addClass(o.stepperType);
            }

            return stepper;
        },

        _createEvents: function(){
            var that = this, element = this.element, o = this.options;

            if (o.buttons) {
                var actions = $("<div/>").addClass("actions").appendTo(element);
                var group_left = $("<div/>").addClass("group-left").appendTo(actions);
                var group_right = $("<div/>").addClass("group-right").appendTo(actions);
                var cancel_button, help_button, prior_button, next_button, finish_button;

                if (o.buttons.cancel) {
                    cancel_button = $("<button type='button'/>").addClass("btn-cancel").html(window.METRO_LOCALES[o.locale].buttons[2]);
                    if (typeof o.buttons.cancel === "boolean") {
                        cancel_button.appendTo(group_left);
                    } else {
                        if (o.buttons.cancel.title) {
                            cancel_button.html(o.buttons.cancel.title);
                        }
                        if (o.buttons.cancel.cls) {
                            cancel_button.addClass(o.buttons.cancel.cls);
                        }
                        if (o.buttons.cancel.group && o.buttons.cancel.group !== "left") {
                            cancel_button.appendTo(group_right);
                        } else {
                            cancel_button.appendTo(group_left);
                        }
                    }
                    cancel_button.on('click', function(){
                        if (typeof  o.onCancel === 'string') {
                            window[o.onCancel](that._currentStep+1, element);
                        } else {
                            o.onCancel(that._currentStep+1, element);
                        }

                    });
                }
                if (o.buttons.help) {
                    help_button = $("<button type='button'/>").addClass("btn-help").html(window.METRO_LOCALES[o.locale].buttons[3]);
                    if (typeof o.buttons.help === "boolean") {
                        help_button.appendTo(group_right);
                    } else {
                        if (o.buttons.help.title) {
                            help_button.html(o.buttons.help.title);
                        }
                        if (o.buttons.help.cls) {
                            help_button.addClass(o.buttons.help.cls);
                        }
                        if (o.buttons.help.group && o.buttons.help.group !== "left") {
                            help_button.appendTo(group_right);
                        } else {
                            help_button.appendTo(group_left);
                        }
                    }
                    help_button.on('click', function(){
                        if (typeof o.onHelp === 'string') {
                            window[o.onHelp](that._currentStep+1, element);
                        } else {
                            o.onHelp(that._currentStep+1, element);
                        }
                    });
                }
                if (o.buttons.prior) {
                    prior_button = $("<button type='button'/>").addClass("btn-prior").html(window.METRO_LOCALES[o.locale].buttons[4]);
                    if (typeof o.buttons.prior === "boolean") {
                        prior_button.appendTo(group_right);
                    } else {
                        if (o.buttons.prior.title) {
                            prior_button.html(o.buttons.prior.title);
                        }
                        if (o.buttons.prior.cls) {
                            prior_button.addClass(o.buttons.prior.cls);
                        }
                        if (o.buttons.prior.group && o.buttons.prior.group !== "left") {
                            prior_button.appendTo(group_right);
                        } else {
                            prior_button.appendTo(group_left);
                        }
                    }
                    prior_button.on('click', function(){
                        if (typeof o.onPrior === 'string') {
                            if (window[o.onPrior](that._currentStep+1, element)) {that.prior();}
                        } else {
                            if (o.onPrior(that._currentStep+1, element)) {that.prior();}
                        }
                    });
                }
                if (o.buttons.next) {
                    next_button = $("<button type='button'/>").addClass("btn-next").html(window.METRO_LOCALES[o.locale].buttons[5]);
                    if (typeof o.buttons.next === "boolean") {
                        next_button.appendTo(group_right);
                    } else {
                        if (o.buttons.next.title) {
                            next_button.html(o.buttons.next.title);
                        }
                        if (o.buttons.next.cls) {
                            next_button.addClass(o.buttons.next.cls);
                        }
                        if (o.buttons.next.group && o.buttons.next.group !== "left") {
                            next_button.appendTo(group_right);
                        } else {
                            next_button.appendTo(group_left);
                        }
                    }
                    next_button.on('click', function(){
                        if (typeof o.onNext === 'string') {
                            if (window[o.onNext](that._currentStep+1, element)) {that.next();}
                        } else {
                            if (o.onNext(that._currentStep+1, element)) {that.next();}
                        }
                    });
                }
                if (o.buttons.finish) {
                    finish_button = $("<button type='button'/>").addClass("btn-finish").html(window.METRO_LOCALES[o.locale].buttons[6]);
                    if (typeof o.buttons.finish === "boolean") {
                        finish_button.appendTo(group_right);
                    } else {
                        if (o.buttons.finish.title) {
                            finish_button.html(o.buttons.finish.title);
                        }
                        if (o.buttons.finish.cls) {
                            finish_button.addClass(o.buttons.finish.cls);
                        }
                        if (o.buttons.finish.group && o.buttons.finish.group !== "left") {
                            finish_button.appendTo(group_right);
                        } else {
                            finish_button.appendTo(group_left);
                        }
                    }
                    finish_button.on('click', function(){
                        if (typeof o.onFinish === 'string') {
                            window[o.onFinish](that._currentStep+1, element);
                        } else {
                            o.onFinish(that._currentStep+1, element);
                        }
                    });
                }
            }
        },

        next: function(){
            var o = this.options;
            var new_step = this._currentStep + 1;

            if (new_step === this._steps.length) {return false;}

            this._currentStep = new_step;
            this._steps.hide();
            $(this._steps[new_step]).show();


            if (typeof o.onPage === 'string') {
                window[o.onPage](this._currentStep + 1, this.element);
            } else {
                o.onPage(this._currentStep + 1, this.element);
            }

            if (this._stepper !== undefined) {this._stepper.stepper('stepTo', this._currentStep + 1);}

            var finish = o.finishStep === 'default' ? this._steps.length - 1 : o.finishStep;
            if (new_step === finish) {
                this.element.find('.btn-finish').attr('disabled', false);
            } else {
                this.element.find('.btn-finish').attr('disabled', true);
            }

            if (new_step === this._steps.length - 1) {
                this.element.find('.btn-next').attr('disabled', true);
            } else {
                this.element.find('.btn-next').attr('disabled', false);
            }

            if (new_step > 0) {
                this.element.find('.btn-prior').attr('disabled', false);
            }

            return true;
        },

        prior: function(){
            var new_step = this._currentStep - 1;
            var o = this.options;

            if (new_step < 0) {return false;}

            this._currentStep = new_step;
            this._steps.hide();
            $(this._steps[new_step]).show();

            if (typeof o.onPage === 'string') {
                window[o.onPage](this._currentStep + 1, this.element);
            } else {
                o.onPage(this._currentStep + 1, this.element);
            }

            if (this._stepper !== undefined) {this._stepper.stepper('stepTo', this._currentStep + 1);}

            var finish = o.finishStep === 'default' ? this._steps.length - 1 : o.finishStep;
            if (new_step === finish) {
                this.element.find('.btn-finish').attr('disabled', false);
            } else {
                this.element.find('.btn-finish').attr('disabled', true);
            }

            if (new_step === 0) {
                this.element.find('.btn-prior').attr('disabled', true);
            } else {
                this.element.find('.btn-prior').attr('disabled', false);
            }

            if (new_step < finish) {
                this.element.find('.btn-next').attr('disabled', false);
            }

            return true;
        },

        stepTo: function(step){
            var new_step = step - 1;
            var o = this.options;

            if (new_step < 0) {return false;}
            this._currentStep = new_step;
            this._steps.hide();
            $(this._steps[new_step]).show();

            if (typeof o.onPage === 'string') {
                window[o.onPage](this._currentStep + 1, this.element);
            } else {
                o.onPage(this._currentStep + 1, this.element);
            }

            if (this._stepper !== undefined) {this._stepper.stepper('stepTo', step);}

            var finish = (o.finishStep === 'default' ? this._steps.length - 1 : o.finishStep);
            if (new_step === finish) {
                this.element.find('.btn-finish').attr('disabled', false);
            } else {
                this.element.find('.btn-finish').attr('disabled', true);
            }

            this.element.find('.btn-next').attr('disabled', (new_step >= finish));
            this.element.find('.btn-prior').attr('disabled', (new_step <= 0));

            return true;
        },

        stepper: function(){
            return this._stepper;
        },

        _destroy: function(){
        },

        _setOption: function(key, value){
            this._super('_setOption', key, value);
        }
    });
})( jQuery );


(function ( $ ) {

    "use strict";

    $.widget( "metro.wizard2" , {

        version: "3.0.0",

        options: {
            start: 1,
            finish: 'default',
            buttonLabels: {
                prev: '&lt;',
                next: '&gt;',
                finish: 'OK',
                help: '?'
            },
            onPrevClick: function(step){return true;},
            onNextClick: function(step){return true;},
            onFinishClick: function(step){},
            onHelpClick: function(step){}
        },

        _step: 1,
        _steps: undefined,

        _create: function () {
            var that = this, element = this.element, o = this.options;

            $.each(element.data(), function(key, value){
                if (key in o) {
                    try {
                        o[key] = $.parseJSON(value);
                    } catch (e) {
                        o[key] = value;
                    }
                }
            });

            this._step = o.start;
            this._steps = element.children('.step');
            this._height = 0;
            this._width = 0;

            if (o.finish === 'default') {
                o.finish = this._steps.length;
            }

            $.each(this._steps, function(i, v){
                if ($(v).outerHeight() > that._height) {that._height = $(v).outerHeight();}
                //console.log(i, $(v).outerHeight(), that._height);
                if ($(v).hasClass('active')) {
                    that._step = i + 1;
                }
            });

            this._width = element.innerWidth() - ( (this._steps.length - 1) * 24 +  (this._steps.length));

            element.children('.step').css({
                height: this._height + 48
            });

            $(window).resize(function(){
                that._width = element.innerWidth() - ( (that._steps.length - 1) * 24 +  (that._steps.length));
                that.step(that._step);
            });

            this._createActionBar();
            this.step(o.start);
            this._placeActionBar();

            element.data('wizard2', this);
        },

        _createActionBar: function(){
            var that = this, element = this.element, o = this.options;
            var bar = $("<div/>").addClass('action-bar').appendTo(element);
            var btn_prev, btn_next, btn_help, btn_finish;

            btn_help = $("<button/>").html(o.buttonLabels.help).addClass('button cycle-button medium-button wiz-btn-help place-left').appendTo(bar);
            btn_finish = $("<button/>").html(o.buttonLabels.finish).addClass('button cycle-button medium-button wiz-btn-finish place-right').appendTo(bar);
            btn_next = $("<button/>").html(o.buttonLabels.next).addClass('button cycle-button medium-button wiz-btn-next place-right').appendTo(bar);
            btn_prev = $("<button/>").html(o.buttonLabels.prev).addClass('button cycle-button medium-button wiz-btn-prev place-right').appendTo(bar);

            btn_help.on('click', function(){
                if (typeof o.onHelpClick === 'string') {
                    window[o.onHelpClick](that._step);
                } else {
                    o.onHelpClick(that._step);
                }
            });

            btn_finish.on('click', function(){
                if (typeof o.onFinishClick === 'string') {
                    window[o.onFinishClick](that._step);
                } else {
                    o.onFinishClick(that._step);
                }
            });

            btn_prev.on('click', function(){
                if (typeof o.onPrevClick === 'string') {
                    if (window[o.onPrevClick](that._step)) {that.prev();}
                } else {
                    if (o.onPrevClick(that._step)) {that.prev();}
                }
            });

            btn_next.on('click', function(){
                if (typeof o.onNextClick === 'string') {
                    if (window[o.onNextClick](that._step)) {that.next();}
                } else {
                    if (o.onNextClick(that._step)) {that.next();}
                }
            });
        },

        _placeActionBar: function(){
            var element = this.element, o = this.options;
            var action_bar = element.find('.action-bar');
            var curr_frame = element.find('.step.active');
            var left = curr_frame.position().left, right = curr_frame.innerWidth();

            action_bar.css({
                left: left,
                width: right
            });
        },

        step: function(index){
            var o = this.options;

            this.element.children('.step')
                .removeClass('active prev next');

            $(this.element.children('.step')[index - 1])
                .addClass('active')
                .css('width', this._width);

            this.element.children('.step.active').prevAll().addClass('prev').css('width', 0);
            this.element.children('.step.active').nextAll().addClass('next').css('width', 0);

            this._placeActionBar();

            if (index === 1) {
                this.element.find('.wiz-btn-prev').hide();
            } else {
                this.element.find('.wiz-btn-prev').show();
            }

            if (index === this._steps.length) {
                this.element.find('.wiz-btn-next').hide();
            } else {
                this.element.find('.wiz-btn-next').show();
            }

            if (index !== o.finish) {
                this.element.find('.wiz-btn-finish').hide();
            } else {
                this.element.find('.wiz-btn-finish').show();
            }

        },

        prev: function(){
            var new_step = this._step - 1;
            if (new_step <= 0) {
                return false;
            }

            this._step = new_step;

            this.step(new_step);

            return true;
        },

        next: function(){
            var new_step = this._step + 1;
            if (new_step > this._steps.length) {return false;}

            this._step = new_step;

            this.step(new_step);

            return true;
        },

        _destroy: function () {
        },

        _setOption: function ( key, value ) {
            this._super('_setOption', key, value);
        }
    });
})( jQuery );