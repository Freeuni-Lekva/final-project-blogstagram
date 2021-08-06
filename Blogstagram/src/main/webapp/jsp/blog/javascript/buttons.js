const addBlogLink = "localhost:8000/addBlog";
const errors = -1;
const added = 4;
const removed = 3;
const edited = 2;

function buttonHtml(buttonId, onClickFunc){
    return `<div class = "container mt-2">
                <button id="${buttonId}" class = "btn btn-info form-control" onclick = "${onClickFunc.name}()">${buttonId}</button>
            </div>`;
}

function addButton(buttonId, place, onClickFunc){
    let add = buttonHtml(buttonId, onClickFunc);
    place.innerHTML += add;
}

function modalButton(){
    return `
        <div class = "container mt-2">
            <button id = "addModerator" class = "btn btn-info form-control" data-toggle = "modal" data-target = "#searchModeratorsModal">addModerator</button>
        </div>
    `
}

function removeModeratorsButton(){
    return `
        <div class = "container mt-2">
            <button id = "removeModerator" class = "btn btn-info form-control" data-toggle = "modal" data-target = "#removeModerators">removeModerator</button>
        </div>
    `
}


function addModeratorButtons(){
    let moderatorsContainer = document.getElementById("moderators_container");
    moderatorsContainer.innerHTML += modalButton();
    moderatorsContainer.innerHTML += removeModeratorsButton();
}

function removeButton(buttonId){
    let edit = document.getElementById(`${buttonId}`);
    edit.parentNode.removeChild(edit);
}

function s(){
    console.log("sda");
}

function removeBlog(blogId, userNickname){
    $.post(`/blog/${blogId}/remove`).then(response => {
        console.log(response);
        let fields = JSON.parse(response);
        let status = fields["status"];
        if(status === removed){
            window.location.href = `/user/${userNickname}`;
        }
    }).catch(errors => {
        console.log(errors);
    });
}

function addAttributes(){
    let title = document.getElementById("title");
    let content = document.getElementById("content");
    title.setAttribute("readonly", "");
    content.setAttribute("readonly", "");
}

function rollBackValues(){
    document.getElementById("title").value = localStorage.getItem("title");
    document.getElementById("content").value = localStorage.getItem("content");
    document.getElementById("hashtags").value = localStorage.getItem("hashtags");
    document.getElementById("blogModerators").value = localStorage.getItem("moderators");
}

function rollback(){
    removeButton("submit");
    removeButton("cancel");
    removeButton("addModerator");
    removeButton("removeModerator");
    let blogContainer = document.getElementById("button_container");
    let remove = document.getElementById("remove_container");
    blogContainer.innerHTML = "";
    addButton("Edit", blogContainer, changeToEdit);
    blogContainer.appendChild(remove);
    rollBackValues();
    addAttributes();
}

function removeAttributes(){
    let title = document.getElementById("title");
    let content = document.getElementById("content");
    title.removeAttribute("readonly");
    content.removeAttribute("readonly");
}

function setUpLocalStorage(){
    let title = document.getElementById("title").value;
    let content = document.getElementById("content").value;
    let hashtag = document.getElementById("hashtags").value;
    let moderators = document.getElementById("blogModerators").value;
    localStorage.setItem("title", title);
    localStorage.setItem("content", content);
    localStorage.setItem("hashtags", hashtag);
    localStorage.setItem("moderators", moderators);
}

function changeToEdit(){
    removeButton("Edit");
    let blogContainer = document.getElementById("button_container");
    let remove = document.getElementById("remove_container");
    console.log(remove);
    blogContainer.innerHTML = "";
    addButton("submit", blogContainer, submit);
    blogContainer.appendChild(remove);
    addButton("cancel", blogContainer, rollback);
    addModeratorButtons();
    setUpLocalStorage();
    removeAttributes();
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


function resetStorage(){
    localStorage.clear();
}

function submit(){
    resetStorage();
    const url = window.location.href;
    const arr = url.split("/");
    let blogId = parseInt(arr[arr.length - 1]);
    let currentTitle = document.getElementById("title").value;
    let currentContent = document.getElementById("content").value;
    let moderatorsJson = getModerators();
    let hashtagsJson = getHashtags();
    let editedContentJson = {"title" : currentTitle, "content" : currentContent,
                        "users" : moderatorsJson["users"],"hashTags" : hashtagsJson["hashTags"]};

    $.post(`/blog/${blogId}/edit`, {
       edited : JSON.stringify(editedContentJson)
    }).then(response => {
        let fields = JSON.parse(response);
        let status = fields["status"];
        console.log(response);
        if(status == edited){
            window.location.href = `/blog/${blogId}`;
        } else if(status == errors){
            let errors = fields["errors"];
            console.log(errors);
            let errorsJson = JSON.parse(errors);
            for(let k = 0; k < errorsJson.length; k++){
                let error = errorsJson[k];
                let {variableName, errorMessage} = error;
                let errContainer = document.getElementById(`err-${variableName}`);
                errContainer.innerText += errorMessage + "\n";
            }
        }
    }).catch(errs => {
        console.log(errs);
    });
}


