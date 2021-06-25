
window.onload = function(){
    console.log("Hi");
    let nicknameInput = document.getElementById("nickname");
    let emailInput = document.getElementById("email");

    console.log(nicknameInput,emailInput);


    emailInput.addEventListener("input",(e) => {
        console.log(emailInput.value);
    });

    nicknameInput.addEventListener("input",(e) => {
        console.log(nicknameInput.value);
    });

}
