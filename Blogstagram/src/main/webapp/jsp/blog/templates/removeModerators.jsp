
<div id = "removeModerators" class = "modal fade" role = "dialog">
    <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h4 class="modal-title text-center">Delete Moderators</h4>
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                    </div>
                    <div id = "modal-users" class="modal-body">
                    </div>
                </div>
    </div>

</div>

<script>

    $("#removeModerators").on("show.bs.modal", function (event) {
        addModeratorsModal();
    });


</script>