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

            ]
        },
        methods: {
            addScope: function() {
                if (this.validateScope(this.form)) {
                    this.scopes.push({
                        operator: this.form.operator === "加法" ? "PLUS" : "MINUS",
                        numberOfQuestions : this.form.numberOfQuestions,
                        minLeftOperand : this.form.minLeftOperand,
                        maxLeftOperand : this.form.maxRightOperand,
                        minRightOperand : this.form.minRightOperand,
                        maxRightOperand : this.form.maxRightOperand,
                        minAnswer : this.form.minAnswer,
                        maxAnswer : this.form.maxAnswer
                    });
                }
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
                        this.testPaper = response.questionList;
                    }, this),
                    contentType: "application/json",
                    dataType: "json"
                });
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
            validateScope : function(form) {
                if (form.numberOfQuestions == null || form.numberOfQuestions <= 0) { return false; }
                if (form.minLeftOperand == null || form.minLeftOperand <= 0) { return false; }
                if (form.maxLeftOperand == null || form.maxLeftOperand <= 0) { return false; }
                if (form.minRightOperand == null || form.minRightOperand <= 0) { return false; }
                if (form.maxRightOperand == null || form.maxRightOperand <= 0) { return false; }
                if (form.minAnswer == null || form.minAnswer <= 0) { return false; }
                if (form.maxAnswer == null || form.maxAnswer <= 0) { return false; }

                if (form.minLeftOperand > form.maxLeftOperand) {
                    $('#minLeftOperand,#maxLeftOperand').addClass('is-invalid');
                    $('#leftOperandValidationError').removeClass('d-none');
                    return false;
                } else {
                    $('#minLeftOperand,#maxLeftOperand').removeClass('is-invalid');
                    $('#leftOperandValidationError').addClass('d-none');
                }
                if (form.minRightOperand > form.maxRightOperand) {
                    $('#minRightOperand,#maxRightOperand').addClass('is-invalid');
                    $('#rightOperandValidationError').removeClass('d-none');
                    return false;
                } else {
                    $('#minRightOperand,#maxRightOperand').removeClass('is-invalid');
                    $('#rightOperandValidationError').addClass('d-none');
                }
                if (form.minAnswer > form.maxAnswer) {
                    $('#minAnswer,#maxAnswer').addClass('is-invalid');
                    $('#answerValidationError').removeClass('d-none');
                    return false;
                } else {
                    $('#minAnswer,#maxAnswer').removeClass('is-invalid');
                    $('#answerValidationError').addClass('d-none');
                }

                return true;
            },
        }
    });
});