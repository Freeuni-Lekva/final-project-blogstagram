function addModerator(nickname){
    console.log("here");
    let activeModerators = document.getElementById("blogModerators").value.split("\n");
    if(!activeModerators.includes(nickname))
        document.getElementById("blogModerators").value += nickname + "\n";
}