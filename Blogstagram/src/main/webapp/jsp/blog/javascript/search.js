function addModerator(nickname){
    let activeModerators = document.getElementById("blogModerators").value.split("\n");
    if(!activeModerators.includes(nickname))
        document.getElementById("blogModerators").value += nickname + "\n";
    console.log(document.getElementById("blogModerators").value);
}