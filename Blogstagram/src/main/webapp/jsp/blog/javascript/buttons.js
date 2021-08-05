const addBlogLink = "localhost:8000/addBlog";
const errors = -1;
const added = 4;

function buttonHtml(buttonId, onClickFunc){
    return `<div class = "container mt-2">
                <button id="${buttonId}" class = "btn btn-info form-control" onclick = "${onClickFunc.name}()">${buttonId}</button>
            </div>`;
}

function addButton(buttonId, place, onClickFunc){
    let add = buttonHtml(buttonId, onClickFunc);
    place.innerHTML += add;
}

function addModeratorButtons(){
    let moderatorsContainer = document.getElementById("moderators_container");
    addButton("addModerator", moderatorsContainer, s);
    addButton("removeModerators", moderatorsContainer, s);
}

function removeButton(buttonId){
    let edit = document.getElementById(`${buttonId}`);
    edit.parentNode.removeChild(edit);
}

function s(){
    console.log("sda");
}

function rollback(){
    removeButton("submit");
    removeButton("cancel");
    removeButton("addModerator");
    removeButton("removeModerators");
    let blogContainer = document.getElementById("button_container");
    blogContainer.innerHTML = "";
    addButton("Edit", blogContainer, changeToEdit)
}

function changeToEdit(){
    removeButton("Edit");
    let blogContainer = document.getElementById("button_container");
    blogContainer.innerHTML = "";
    addButton("submit", blogContainer, s);
    addButton("cancel", blogContainer, rollback);
    addModeratorButtons();
}

function getModerators(){
    let moderators = document.getElementById("blogModerators").value;
    const moderator_arr = moderators.split("\n");
    let moderatorsJsonArr = [];
    for(let k = 0; k < moderator_arr.length; k++){
        if(moderator_arr[k] === "") continue;
        moderatorsJsonArr.push({"nickname" : moderator_arr[k]});
    }
    let moderatorsJson = {};
    moderatorsJson["users"] = moderatorsJsonArr;
    return moderatorsJson;
}

function getHashtags(){
    let hashtags = document.getElementById("hashtags").value;
    const hashtagsArr = hashtags.split("\n");
    let hashtagsJsonArr = [];
    for(let k = 0; k < hashtagsArr.length; k++){
        if(hashtagsArr[k] === "") continue;
        hashtagsJsonArr.push({"hashTag": hashtagsArr[k]});
    }
    let hashtagsJson = {};
    hashtagsJson["hashTags"] = hashtagsJsonArr;
    return hashtagsJson;
}

function addHashTag(){
    let content = document.getElementById("content").value;
    document.getElementById("hashtags").value = "";
    let used = [];
    for(let k = 0; k < content.length; k++){
        if(content[k] == '#'){
            let hashtag = "";
            for(let j = k + 1; j < content.length; j++){
                if(content[j] == " "){
                    k = j;
                    break;
                }
                hashtag += content[j];
            }
            if(hashtag == "" || used.includes(hashtag)) continue;
            document.getElementById("hashtags").value += hashtag + "\n";
            used.push(hashtag);
        }
    }
}



function refreshFields(){
    let arr = ["content", "title", "moderator"];
    for(let field of arr){
        let err = document.getElementById(`err-${field}`);
        err.innerText = "";
    }
}

function addBlog(){
    refreshFields();
    let currentTitle = document.getElementById("title").value;
    let currentContent = document.getElementById("content").value;
    let moderatorsJson = getModerators();
    let hashtagsJson = getHashtags();
    $.post("/addBlog", {
        title: currentTitle,
        content: currentContent,
        moderators: JSON.stringify(moderatorsJson),
        hashtags: JSON.stringify(hashtagsJson)
    }).then(response => {
        let fields = JSON.parse(response);
        let status = fields['status'];
        if(status === added){
            window.location.href = `/blog/${fields["blogId"]}`;
        } else if(status == errors){
            let errors = fields["errors"];
            let errorsJson = JSON.parse(errors);
            for(let k = 0; k < errorsJson.length; k++){
                let error = errorsJson[k];
                let {variableName, errorMessage} = error;
                let errContainer = document.getElementById(`err-${variableName}`);
                errContainer.innerText += errorMessage + "\n";
            }
        }
    }).catch(errors => {
        console.log(errors);
    });
}