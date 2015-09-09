(function() {
  var showErrorsModule;

  showErrorsModule = angular.module('ui.bootstrap.showErrors', []);

  showErrorsModule.directive('showErrors', [
    '$timeout', 'showErrorsConfig', '$interpolate', '$filter', function($timeout, showErrorsConfig, $interpolate, $filter) {
      var getShowSuccess, getShowWarning, getTrigger, linkFn;
      getTrigger = function(options) {
        var trigger;
        trigger = showErrorsConfig.trigger;
        if (options && (options.trigger != null)) {
          trigger = options.trigger;
        }
        return trigger;
      };
      getShowSuccess = function(options) {
        var showSuccess;
        showSuccess = showErrorsConfig.showSuccess;
        if (options && (options.showSuccess != null)) {
          showSuccess = options.showSuccess;
        }
        return showSuccess;
      };
      getShowWarning = function(options) {
        var showWarning;
        showWarning = showErrorsConfig.showWarning;
        if (options && (options.showWarning != null)) {
          showWarning = options.showWarning;
        }
        return showWarning;
      };
      linkFn = function(scope, el, attrs, formCtrl) {
        var blurred, inputEl, inputName, inputNgEl, options, showSuccess, toggleClasses, showWarning, trigger;
        blurred = false;
        options = scope.$eval(attrs.showErrors);
        showSuccess = getShowSuccess(options);
        showWarning = getShowWarning(options);
        trigger = getTrigger(options);
        inputEl = el[0].querySelector('.form-control[name]');
        inputNgEl = angular.element(inputEl);
        inputName = $interpolate(inputNgEl.attr('name') || '')(scope);
        if (!inputName) {
          throw "show-errors element has no child input elements with a 'name' attribute and a 'form-control' class";
        }
        inputNgEl.bind(trigger, function() {
          blurred = true;
          return toggleClasses(formCtrl[inputName].$invalid);
        });
        scope.$watch(function() {
          return formCtrl[inputName] && formCtrl[inputName].$invalid;
        }, function(invalid) {
          if (!blurred) {
            return;
          }
          return toggleClasses(invalid);
        });
        scope.$on('show-errors-check-validity', function() {
          return toggleClasses(formCtrl[inputName].$invalid);
        });
        scope.$on('show-errors-reset', function() {
          return $timeout(function() {
            el.removeClass('has-error');
            el.removeClass('has-success');
            el.removeClass('has-warning');
            return blurred = false;
          }, 0, false);
        });
        return toggleClasses = function(invalid) {
          if(invalid && !(_.isEqual(scope.data[inputName], formCtrl[inputName].$modelValue) || scope.data[inputName] == formCtrl[inputName].$modelValue)) {
            el.find("span").remove();
            el.append('<span class="glyphicon glyphicon-remove form-control-feedback"></span>');
            return el.toggleClass('has-error', invalid);
          } else if(showWarning && (_.isEqual(scope.data[inputName], formCtrl[inputName].$modelValue) || scope.data[inputName] == formCtrl[inputName].$modelValue)) {
            el.find("span").remove();
            el.find(".warning").remove();
            el.append('<span class="glyphicon glyphicon-warning-sign form-control-feedback"></span>');
            el.append('<small class="help-block warning">Current person ' + $filter('accountFilter')(inputName)  + '</small>');
            return el.toggleClass('has-warning', !invalid);
          } else if(!invalid && showSuccess) {
            el.find("span").remove();
            el.find(".warning").remove();
            el.append('<span class="glyphicon glyphicon-ok form-control-feedback"></span>');
            return el.toggleClass('has-success', !invalid);
          }
        };
      };
      return {
        restrict: 'A',
        require: '^form',
        compile: function(elem, attrs) {
          if (attrs['showErrors'].indexOf('skipFormGroupCheck') === -1) {
            if (!(elem.hasClass('form-group') || elem.hasClass('input-group'))) {
              throw "show-errors element does not have the 'form-group' or 'input-group' class";
            }
          }
          return linkFn;
        }
      };
    }
  ]);

  showErrorsModule.provider('showErrorsConfig', function() {
    var _showSuccess, _trigger;
    _showSuccess = false;
    _trigger = 'blur';
    this.showSuccess = function(showSuccess) {
      return _showSuccess = showSuccess;
    };
    this.trigger = function(trigger) {
      return _trigger = trigger;
    };
    this.$get = function() {
      return {
        showSuccess: _showSuccess,
        trigger: _trigger
      };
    };
  });

}).call(this);
