//get all the operators objects
var optBtn = document.querySelectorAll('.operator input');
//operators array
var operators = ['+', '-', '*', '/'];
for (var i = 0; i < optBtn.length; i++) {
    //operator buttons event handler
    optBtn[i].onclick = function (e) {
        //get input values and get get answer element
        var inputX = parseInt(document.getElementById("x").value);
        var inputY = parseInt(document.getElementById("y").value);
        var ansText = document.getElementById("Answer");

        //determine which operation to do
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