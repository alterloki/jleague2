function filter(data, name, index) {
    var min = parseInt($('#min' + name).val());
    var max = parseInt($('#max' + name).val());
    var value = parseFloat(data[index]) || 0;

    if (( isNaN(min) && isNaN(max) ) ||
        ( isNaN(min) && value <= max ) ||
        ( min <= value && isNaN(max) ) ||
        ( min <= value && value <= max )) {
        return true;
    }
    return false;
}

function filterPosition(data) {
    var pos = $('#position').val();
    var value = data[1];
    if (pos == '*' || pos == value) {
        return true;
    }
    return false;
}

function filterSV(data, letter, id) {
    var min = parseInt($('#min' + id).val());
    var max = parseInt($('#max' + id).val());
    var regexp = new RegExp(letter + '(\\d+)');
    var p = regexp.exec(data[8]);
    var value = 20;
    if (p) {
        value = parseFloat(p[1]) || 20;
    }
    if (( isNaN(min) && isNaN(max) ) ||
        ( isNaN(min) && value <= max ) ||
        ( min <= value && isNaN(max) ) ||
        ( min <= value && value <= max )) {
        return true;
    }
    return false;
}

$.fn.dataTable.ext.search.push(
    function (settings, data, dataIndex) {
        if (filter(data, "Strength", 7) &&
            filter(data, "Talent", 6) &&
            filter(data, "Age", 5) &&
            filter(data, "Health", 9) &&
            filter(data, "Price", 12) &&
            filter(data, "Salary", 10) &&
            filterSV(data, "у", "U") &&
            filterSV(data, "т", "T") &&
            filterSV(data, "р", "R") &&
            filterSV(data, "п", "P") &&
            filterSV(data, "н", "N") &&
            filterSV(data, "д", "D") &&
            filterSV(data, "о", "O") &&
            filterSV(data, "вг", "VG") &&
            filterSV(data, "с", "S") &&
            filterSV(data, "ф", "F") &&
            filterPosition(data)) {
            return true;
        }
        return false;
    }
);


