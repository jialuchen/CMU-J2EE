var optBtn = document.querySelectorAll('.operator input');
var operators = ['+', '-', 'x', '÷'];
for (var i = 0; i < optBtn.length; i++) {
    optBtn[i].onclick = function (e) {
        var inputX = parseInt(document.getElementById("x").value);
        var inputY = parseInt(document.getElementById("y").value);
        var ansText = document.getElementById("Answer");

        if (this.value == operators[0]) {
            ansText.value = inputX + inputY;
        } else if(this.value == operators[1]) {
            ansText.value = inputX - inputY;
        } else if(this.value == operators[2]) {
            ansText.value = inputX * inputY;
        } else if(this.value == operators[3]) {
            ansText.value = inputX / inputY;
        }
    }

}