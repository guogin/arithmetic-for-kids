var app;

$(function() {
    app = new Vue({
        el: '#app',
        data: {
            simpleForm : {
                operator: "PLUS",
                numberOfQuestions : null,
                numberOfDigits : 1,
                isCarryOrBorrowEnabled : true
            },
            advancedForm : {
                operator: "PLUS",
                numberOfQuestions : null,
                minLeftOperand : null,
                maxLeftOperand : null,
                minRightOperand : null,
                maxRightOperand : null,
                minAnswer : null,
                maxAnswer : null
            },
            userSettings : {
                simpleScopes : [],
                advancedScopes : []
            },
            questions : [

            ],
            tooManyScopes : false,
            showAnswer : false,
            settingMode: 'simple'
        },
        methods: {
            switchSettingModeTo : function(mode) {
                this.settingMode = mode;
            },
            addScope: function() {
                if (this.settingMode === 'advanced') {
                    if (!this.validateForm(this.advancedForm)) {
                        return;
                    }
                } else {
                    if (!this.validateForm(this.simpleForm)) {
                        return;
                    }
                }
                if (this.userAddedTooManyScopes()) {
                    this.tooManyScopes = true;
                    return;
                }
                if (this.settingMode === 'advanced') {
                    this.userSettings.advancedScopes.push({
                        operator: this.advancedForm.operator,
                        numberOfQuestions: this.advancedForm.numberOfQuestions,
                        minLeftOperand: this.advancedForm.minLeftOperand,
                        maxLeftOperand: this.advancedForm.maxLeftOperand,
                        minRightOperand: this.advancedForm.minRightOperand,
                        maxRightOperand: this.advancedForm.maxRightOperand,
                        minAnswer: this.advancedForm.minAnswer,
                        maxAnswer: this.advancedForm.maxAnswer
                    });
                } else {
                    this.userSettings.simpleScopes.push({
                        operator : this.simpleForm.operator,
                        numberOfQuestions : this.simpleForm.numberOfQuestions,
                        numberOfDigits : this.simpleForm.numberOfDigits,
                        carryOrBorrowEnabled : this.simpleForm.isCarryOrBorrowEnabled
                    });
                }
            },
            removeScope: function(index) {
                if (this.settingMode === 'advanced') {
                    this.userSettings.advancedScopes.splice(index, 1);
                } else {
                    this.userSettings.simpleScopes.splice(index, 1);
                }
                this.tooManyScopes = this.userAddedTooManyScopes();
            },
            generateTestPaper: function() {
                var parameter = {};
                if (this.settingMode === 'advanced') {
                    if (this.userSettings.advancedScopes.length === 0) {
                        return;
                    }
                    parameter = {
                        simpleScopes : [],
                        advancedScopes : this.userSettings.advancedScopes
                    };
                } else {
                    if (this.userSettings.simpleScopes.length === 0) {
                        return;
                    }
                    parameter = {
                        simpleScopes : this.userSettings.simpleScopes,
                        advancedScopes : []
                    };
                }

                $.ajax({
                    type: "POST",
                    url: "/api/generateTestPaper",
                    data: JSON.stringify(parameter),
                    success: $.proxy(function (data) {
                        this.questions = [];
                        for (var i = 0; i < data.questionList.length; i += 2) {
                            var i1 = i, i2 = i + 1;
                            if (i2 < data.questionList.length) {
                                this.questions.push([
                                    data.questionList[i1], data.questionList[i2]
                                ]);
                            } else {
                                this.questions.push([
                                    data.questionList[i1], null // padding for last row
                                ]);
                            }
                        }
                    }, this),
                    error : $.proxy(function(xhr, textStatus, errorThrown) {
                        $('#error-message').text(xhr.responseJSON.message || "服务器检查了你的参数，它总觉得哪里不对，但又说不上来...")
                        $('#error-message-dialog').modal();
                    }, this),
                    contentType: "application/json",
                    dataType: "json"
                });
            },
            onOperatorChange: function(event) {
                if (this.advancedForm.operator === "PLUS") {
                    $('#labelLeftOperand').text("加数");
                    $('#labelRightOperand').text("加数");
                    $('#labelAnswer').text("和");
                } else {
                    $('#labelLeftOperand').text("被减数");
                    $('#labelRightOperand').text("减数");
                    $('#labelAnswer').text("差");
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
                    scope.minAnswer +
                    " ~ " +
                    scope.maxAnswer;
            },
            scopeNumberOfDigits : function(scope) {
                return scope.numberOfDigits + "位数";
            },
            scopeIsCarryOrBorrowEnabled : function(scope) {
                return scope.operator === "PLUS" ?
                    (scope.carryOrBorrowEnabled ? "带进位" : "不带进位") :
                    (scope.carryOrBorrowEnabled ? "带退位" : "不带退位");
            },
            validateForm : function(form) {
                if (this.settingMode === 'advanced') {
                    if (form.numberOfQuestions == null || form.numberOfQuestions <= 0) {
                        return false;
                    }
                    if (form.minLeftOperand == null || form.minLeftOperand <= 0) {
                        return false;
                    }
                    if (form.maxLeftOperand == null || form.maxLeftOperand <= 0) {
                        return false;
                    }
                    if (!this.validateMinMax(form, "minLeftOperand", "maxLeftOperand")) {
                        return false;
                    }
                    if (form.minRightOperand == null || form.minRightOperand <= 0) {
                        return false;
                    }
                    if (!this.validateMinMax(form, "minRightOperand", "maxRightOperand")) {
                        return false;
                    }
                    if (form.minAnswer == null || form.minAnswer <= 0) {
                        return false;
                    }
                    if (form.maxAnswer == null || form.maxAnswer <= 0) {
                        return false;
                    }
                    if (!this.validateMinMax(form, "minAnswer", "maxAnswer")) {
                        return false;
                    }
                } else {
                    if (form.numberOfQuestions == null || form.numberOfQuestions <= 0) {
                        return false;
                    }
                }
                return true;
            },
            validateMinMax : function(advancedForm, minProperty, maxProperty) {
                var idField = "#" + minProperty + "," + "#" + maxProperty;
                var idErr = "#" + minProperty.substring(3);

                if (parseInt(advancedForm[minProperty], 10) > parseInt(advancedForm[maxProperty], 10)) {
                    $(idField).addClass('is-invalid');
                    $(idErr).removeClass('d-none');
                    return false;
                } else {
                    $(idField).removeClass('is-invalid');
                    $(idErr).addClass('d-none');
                }

                return true;
            },
            userAddedTooManyScopes : function() {
                return (this.userSettings.advancedScopes.length >= 5) || (this.userSettings.simpleScopes.length >= 5);
            },
            formatQuestion : function(question) {
                if (question === null) {
                    return "";
                }
                return question.expression + ( this.showAnswer ? question.answer : "");
            },
            toggleAnswer : function() {
                this.showAnswer = !this.showAnswer;
                $('#toggle-answer-btn').text(this.showAnswer? "隐藏答案" : "显示答案");
            },
            exportToPdf : function() {
                if (this.questions.length > 0) {
                    var tableData = this.questions.map($.proxy(function(row) {
                        return {
                            col1: this.formatQuestion(row[0]),
                            col2: this.formatQuestion(row[1])
                        };
                    }, this));

                    var doc = new jsPDF();
                    doc.table(1, 1, tableData, undefined, {autoSize: true, printHeaders: false});
                    doc.save("questions.pdf", undefined);
                }
            }
        }
    });
});