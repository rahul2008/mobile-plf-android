function format(d) {
    // `d` is the original data object for the row
    return '<table cellpadding="5" cellspacing="0" border="1" style="padding-left:50px;">'
        + '<tr>' + '<td>Console Output:</td>' + '<td>' + d.StdOutput + '</td>' + '</tr>' 
        //+ '<tr>' + '<td>Failure Message:</td>' + '<td>' + d.TraceOutput + '</td>' + '</tr>'
        + '</table>';
}

// Add event listener for opening and closing details
$('#example tbody').on('click', 'td.details-control', function () {
    var tr = $(this).closest('tr');
    var row = table.row(tr);
    if (row.child.isShown()) {
        // This row is already open - close it
        row.child.hide();
        tr.removeClass('shown');
    }
    else {
        // Open this row
        row.child(format(row.data())).show();
        tr.addClass('shown');
    }
});
