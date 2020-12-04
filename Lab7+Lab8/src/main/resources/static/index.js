const TaskStatus = ["BLOCKED", "CLOSED", "IN_PROGRESS", "OPEN"];
const TaskSeverity = ["LOW", "NORMAL", "HIGH"];

 function getTasks(){
    $.ajax({
        type:"GET",
        url:"api/tasks",
        headers:{
            "X-Fields":"id,title,status",
            "X-Sort":"status"
        },
        success: function(tasks){
            showTasks(tasks);
        }
     });
 }
 function showTasks(tasks){
    var header ='';
    var body = '';
    var index = 0;
    $.each(TaskStatus, function(i, status){
        header += '<td width ="25%"><b>' + status + '</b></td>';
        body += '<td height="100%" class="' + status + '">';
        while(tasks != undefined && index < tasks.length && tasks[index].status === status){
            body += String.format('<p><div onclick =\'javascript:getTask("{0}")\' class="{1}">{2}</div></p>',
            tasks[index].id, status, tasks[index].title);
           index++;
        }
        body +='</td>';
    });
    $('body').html(String.format('<table height="100%">' +
    '<tr>'+
    '<td colspan="4" width="100%">'
    + '<input type="button" value="Add" onclick=\'showTask({id:""})\'>'
    + '<td>'
    + '</tr>'
    + "<tr>{0}</tr>"
    + "<tr>{1}</tr>", header, body));
 }

 function getTask(id){
    $.ajax({
        type:"GET",
        url:"api/tasks/" + id,
        success: function(task){
            showTask(task);
        }
        });
 }
  function showTask(task){
    var html = String.format('<table>'
    + '<input id="id" type="hidden" value="{0}">'
    + textEntry(task, "title", "Title")
    + textEntry(task, "assignedTo", "Assigned To")
     + comboEntry(task, "status", "Status", TaskStatus)
    + comboEntry(task, "severity", "Severity", TaskSeverity)
    + '<tr><td colspan="2" width="100%" align="center">'
    + '<input type ="button"  value="Save" onclick ="saveTask()">',
    task.id);
    if(task.id.length>0){
        html += '<input type="button" value="Remove" onclick="removeTask()">';
    }
     html += '<input type="button" value="Cancel" onclick="getTasks()">'
     + '</td></tr></table>';

    $('body').html(html);
  }

  function textEntry(task, field, label){
    return String.format('<tr>'
    + '<td style ="text-align:right" nowrap> {0}</td>'
    + '<td width="100%">'
    +'<input id="{1}" value="{2}">'
    +'</td>'
    +'</tr>',
    label, field, task[field] !== undefined ? task[field] :""
    );
  }

function comboEntry(task, field, label, values) {
   var html = String.format('<tr>'
      + '<td style="text-align:right" nowrap>{0}</td>'
      + '<td width="100%">'
      + '<select id="{1}">',
      label, field);
   $.each(values, function(i, value) {
      html += String.format('<option{0}>{1}</option>',
         task[field] === value ? ' selected' : '', value);
   });
   html += '</td></tr>';
   return html;
}

function saveTask(){
    $.ajax({
        type: $("#id").val().length>0 ? "PUT" : "POST",
        url: "api/tasks/" + $("#id").val(),
        contentType: "application/json",
        data: JSON.stringify({
            title: $("#title").val(),
            assignedTo: $("#assignedTo").val(),
            severity: $("#severity").val(),
            status: $("#status").val()
        }),
        success:function(){
            getTasks();
        }
    });
}
function removeTask(){
    if(confirm("Are you sure you want to delete the task?")){
      $.ajax({
          type:"DELETE",
          url:"api/tasks/" + $("#id").val(),
          success:function(){
            getTasks();
          }
      });
    }
}

 $(document).ready(function(){
    getTasks();
 });