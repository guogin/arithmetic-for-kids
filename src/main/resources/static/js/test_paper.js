var app;

$(function() {
    app = new Vue({
        el: '#app',
        data: {
            simpleForm : {
                operator: "加法",
                numberOfQuestions : null,
                numberOfDigits : null,
                hasCarryOrBorrow : true
            },
            advancedForm : {
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
                if (!this.validateForm(this.advancedForm)) { return; }
                if (!this.checkNumberOfScopes()) { return; }
                this.scopes.push({
                    operator: this.advancedForm.operator === "加法" ? "PLUS" : "MINUS",
                    numberOfQuestions : this.advancedForm.numberOfQuestions,
                    minLeftOperand : this.advancedForm.minLeftOperand,
                    maxLeftOperand : this.advancedForm.maxLeftOperand,
                    minRightOperand : this.advancedForm.minRightOperand,
                    maxRightOperand : this.advancedForm.maxRightOperand,
                    minAnswer : this.advancedForm.minAnswer,
                    maxAnswer : this.advancedForm.maxAnswer
                });
            },
            removeScope: function(index) {
                this.scopes.splice(index, 1);
                this.checkNumberOfScopes();
            },
            generateTestPaper: function() {
                if (this.scopes.length > 0) {
                    $.ajax({
                        type: "POST",
                        url: "/api/generateTestPaper",
                        data: JSON.stringify(this.scopes),
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
                }
            },
            onOperatorChange: function(event) {
                if (this.advancedForm.operator === "加法") {
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
            validateForm : function(advancedForm) {
                if (advancedForm.numberOfQuestions == null || advancedForm.numberOfQuestions <= 0) { return false; }
                if (advancedForm.minLeftOperand == null || advancedForm.minLeftOperand <= 0) { return false; }
                if (advancedForm.maxLeftOperand == null || advancedForm.maxLeftOperand <= 0) { return false; }
                if (!this.validateMinMax(advancedForm, "minLeftOperand", "maxLeftOperand")) { return false; }
                if (advancedForm.minRightOperand == null || advancedForm.minRightOperand <= 0) { return false; }
                if (!this.validateMinMax(advancedForm, "minRightOperand", "maxRightOperand")) { return false; }
                if (advancedForm.minAnswer == null || advancedForm.minAnswer <= 0) { return false; }
                if (advancedForm.maxAnswer == null || advancedForm.maxAnswer <= 0) { return false; }
                if (!this.validateMinMax(advancedForm, "minAnswer", "maxAnswer")) { return false; }

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
            checkNumberOfScopes : function() {
                this.tooManyScopes = (this.scopes.length >= 5);
                return !this.tooManyScopes;
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