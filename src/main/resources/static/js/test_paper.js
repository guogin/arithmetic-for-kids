var app;

$(function() {
    app = new Vue({
        el: '#app',
        data: {
            form : {
                operator: "加法",
                numberOfQuestions : null,
                minLeftOperand : null,
                maxLeftOperand : null,
                minRightOperand : null,
                maxRightOperand : null,
                minAnswer : null,
                maxAnswer : null
            },
            scopes : [

            ],
            testPaper : [

            ],
            tooManyScopes : false
        },
        methods: {
            addScope: function() {
                if (!this.validateForm(this.form)) { return; }
                if (!this.validateScope()) { return; }
                this.scopes.push({
                    operator: this.form.operator === "加法" ? "PLUS" : "MINUS",
                    numberOfQuestions : this.form.numberOfQuestions,
                    minLeftOperand : this.form.minLeftOperand,
                    maxLeftOperand : this.form.maxLeftOperand,
                    minRightOperand : this.form.minRightOperand,
                    maxRightOperand : this.form.maxRightOperand,
                    minAnswer : this.form.minAnswer,
                    maxAnswer : this.form.maxAnswer
                });
            },
            removeScope: function(index) {
                this.scopes.splice(index, 1);
            },
            generateTestPaper: function() {
                $.ajax({
                    type: "POST",
                    url: "/api/generateTestPaper",
                    data: JSON.stringify(this.scopes),
                    success: $.proxy(function (response) {
                        for (var i = 0; i < response.questionList.length; i+=2) {
                            var i1 = i, i2 = i + 1;
                            if (i2 < response.questionList.length) {
                                this.testPaper.push([
                                    response.questionList[i1], response.questionList[i2]
                                ]);
                            } else {
                                this.testPaper.push([
                                    response.questionList[i1], this.emptyQuestion() // padding for last row
                                ]);
                            }
                        }
                    }, this),
                    contentType: "application/json",
                    dataType: "json"
                });
            },
            onOperatorChange: function(event) {
                if (this.form.operator === "加法") {
                    $('#labelLeftOperand').text("加数范围");
                    $('#labelRightOperand').text("加数范围");
                    $('#labelAnswer').text("求和范围");
                } else {
                    $('#labelLeftOperand').text("被减数范围");
                    $('#labelRightOperand').text("减数范围");
                    $('#labelAnswer').text("求差范围");
                }
            },
            scopeTitle : function(scope) {
                return ( scope.operator === "PLUS" ? "加法" : "减法" ) +
                    scope.numberOfQuestions + "道";
            },
            scopeLeftOperandRange : function(scope) {
                return ( scope.operator === "PLUS" ? "加数" : "被减数" ) +
                    ": " +
                    scope.minLeftOperand +
                    " ~ " +
                    scope.maxLeftOperand;
            },
            scopeRightOperandRange : function(scope) {
                return ( scope.operator === "PLUS" ? "加数" : "减数" ) +
                    ": " +
                    scope.minRightOperand +
                    " ~ " +
                    scope.maxRightOperand;
            },
            scopeAnswerRange : function(scope) {
                return ( scope.operator === "PLUS" ? "和" : "差" ) +
                    ": " +
                    scope.minRightOperand +
                    " ~ " +
                    scope.maxRightOperand;
            },
            validateForm : function(form) {
                if (form.numberOfQuestions == null || form.numberOfQuestions <= 0) { return false; }
                if (form.minLeftOperand == null || form.minLeftOperand <= 0) { return false; }
                if (form.maxLeftOperand == null || form.maxLeftOperand <= 0) { return false; }
                if (!this.validateMinMax(form, "minLeftOperand", "maxLeftOperand")) { return false; }
                if (form.minRightOperand == null || form.minRightOperand <= 0) { return false; }
                if (!this.validateMinMax(form, "minRightOperand", "maxRightOperand")) { return false; }
                if (form.minAnswer == null || form.minAnswer <= 0) { return false; }
                if (form.maxAnswer == null || form.maxAnswer <= 0) { return false; }
                if (!this.validateMinMax(form, "minAnswer", "maxAnswer")) { return false; }

                return true;
            },
            validateMinMax : function(form, minProperty, maxProperty) {
                var idField = "#" + minProperty + "," + "#" + maxProperty;
                var idErr = "#" + minProperty.substring(3);

                if (parseInt(form[minProperty], 10) > parseInt(form[maxProperty], 10)) {
                    $(idField).addClass('is-invalid');
                    $(idErr).removeClass('d-none');
                    return false;
                } else {
                    $(idField).removeClass('is-invalid');
                    $(idErr).addClass('d-none');
                }

                return true;
            },
            validateScope : function() {
                this.tooManyScopes = (this.scopes.length >= 5);
                return !this.tooManyScopes;
            },
            emptyQuestion : function() {
                return { operator : "", leftOperand : "", rightOperand : "", answer : "" };
            }
        }
    });
});