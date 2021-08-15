function getModeratorDiv(moderatorNickname){
    return `
        <div id = "${moderatorNickname}" class = "container media border py-2 my-2">
            <div class = "media-body text-center row">
                <h4 class = "col">${moderatorNickname}</h4>
                <button type="button" class="close col" onclick="removeModerator('${moderatorNickname}', this)">&times;</button>
            </div>
        </div>
    `
}

function constructModerators(currentModerators){
    let value = "";
    for(let k = 0; k < currentModerators.length; k++){
        if(currentModerators[k] === "" || currentModerators[k] === " ") continue;
        value += currentModerators[k] + "\n";
    }
    document.getElementById("blogModerators").value = value;
}

function removeModerator(moderatorNickname, tag){
   let currentModerators = document.getElementById("blogModerators").value.split("\n");
   let indexOfElem = currentModerators.indexOf(moderatorNickname);
   if(indexOfElem != -1) currentModerators.splice(indexOfElem, 1);
   constructModerators(currentModerators);
   let child = tag.parentNode.parentNode;
   child.parentNode.removeChild(child);
}

function addModeratorsModal(){
    let currentModerators = document.getElementById("blogModerators").value.split("\n");
    console.log(document.getElementById("blogModerators").value);
    if(currentModerators.length == 0 || (currentModerators.length ==  1 && currentModerators[0] === "")) return;
    let modalBody = document.getElementById("modal-users");
    modalBody.innerHTML = "";
    for(let k = 0; k < currentModerators.length; k++){
        if(currentModerators[k] === "" || currentModerators[k] === " ") continue;
        modalBody.innerHTML += getModeratorDiv(currentModerators[k]);
    }
}