/*
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
var showControllersOnly = false;
var seriesFilter = "";
var filtersOnlySampleSeries = true;

/*
 * Add header in statistics table to group metrics by category
 * format
 *
 */
function summaryTableHeader(header) {
    var newRow = header.insertRow(-1);
    newRow.className = "tablesorter-no-sort";
    var cell = document.createElement('th');
    cell.setAttribute("data-sorter", false);
    cell.colSpan = 1;
    cell.innerHTML = "Requests";
    newRow.appendChild(cell);

    cell = document.createElement('th');
    cell.setAttribute("data-sorter", false);
    cell.colSpan = 3;
    cell.innerHTML = "Executions";
    newRow.appendChild(cell);

    cell = document.createElement('th');
    cell.setAttribute("data-sorter", false);
    cell.colSpan = 6;
    cell.innerHTML = "Response Times (ms)";
    newRow.appendChild(cell);

    cell = document.createElement('th');
    cell.setAttribute("data-sorter", false);
    cell.colSpan = 1;
    cell.innerHTML = "Throughput";
    newRow.appendChild(cell);

    cell = document.createElement('th');
    cell.setAttribute("data-sorter", false);
    cell.colSpan = 2;
    cell.innerHTML = "Network (KB/sec)";
    newRow.appendChild(cell);
}

/*
 * Populates the table identified by id parameter with the specified data and
 * format
 *
 */
function createTable(table, info, formatter, defaultSorts, seriesIndex, headerCreator) {
    var tableRef = table[0];

    // Create header and populate it with data.titles array
    var header = tableRef.createTHead();

    // Call callback is available
    if(headerCreator) {
        headerCreator(header);
    }

    var newRow = header.insertRow(-1);
    for (var index = 0; index < info.titles.length; index++) {
        var cell = document.createElement('th');
        cell.innerHTML = info.titles[index];
        newRow.appendChild(cell);
    }

    var tBody;

    // Create overall body if defined
    if(info.overall){
        tBody = document.createElement('tbody');
        tBody.className = "tablesorter-no-sort";
        tableRef.appendChild(tBody);
        var newRow = tBody.insertRow(-1);
        var data = info.overall.data;
        for(var index=0;index < data.length; index++){
            var cell = newRow.insertCell(-1);
            cell.innerHTML = formatter ? formatter(index, data[index]): data[index];
        }
    }

    // Create regular body
    tBody = document.createElement('tbody');
    tableRef.appendChild(tBody);

    var regexp;
    if(seriesFilter) {
        regexp = new RegExp(seriesFilter, 'i');
    }
    // Populate body with data.items array
    for(var index=0; index < info.items.length; index++){
        var item = info.items[index];
        if((!regexp || filtersOnlySampleSeries && !info.supportsControllersDiscrimination || regexp.test(item.data[seriesIndex]))
                &&
                (!showControllersOnly || !info.supportsControllersDiscrimination || item.isController)){
            if(item.data.length > 0) {
                var newRow = tBody.insertRow(-1);
                for(var col=0; col < item.data.length; col++){
                    var cell = newRow.insertCell(-1);
                    cell.innerHTML = formatter ? formatter(col, item.data[col]) : item.data[col];
                }
            }
        }
    }

    // Add support of columns sort
    table.tablesorter({sortList : defaultSorts});
}

$(document).ready(function() {

    // Customize table sorter default options
    $.extend( $.tablesorter.defaults, {
        theme: 'blue',
        cssInfoBlock: "tablesorter-no-sort",
        widthFixed: true,
        widgets: ['zebra']
    });

    var data = {"OkPercent": 100.0, "KoPercent": 0.0};
    var dataset = [
        {
            "label" : "KO",
            "data" : data.KoPercent,
            "color" : "#FF6347"
        },
        {
            "label" : "OK",
            "data" : data.OkPercent,
            "color" : "#9ACD32"
        }];
    $.plot($("#flot-requests-summary"), dataset, {
        series : {
            pie : {
                show : true,
                radius : 1,
                label : {
                    show : true,
                    radius : 3 / 4,
                    formatter : function(label, series) {
                        return '<div style="font-size:8pt;text-align:center;padding:2px;color:white;">'
                            + label
                            + '<br/>'
                            + Math.round10(series.percent, -2)
                            + '%</div>';
                    },
                    background : {
                        opacity : 0.5,
                        color : '#000'
                    }
                }
            }
        },
        legend : {
            show : true
        }
    });

    // Creates APDEX table
    createTable($("#apdexTable"), {"supportsControllersDiscrimination": true, "overall": {"data": [0.8301642178046672, 500, 1500, "Total"], "isController": false}, "titles": ["Apdex", "T (Toleration threshold)", "F (Frustration threshold)", "Label"], "items": [{"data": [0.5, 500, 1500, "POST - \/api (1)"], "isController": false}, {"data": [0.0, 500, 1500, "POST - \/api (10)"], "isController": false}, {"data": [0.0, 500, 1500, "POST - \/api (20)"], "isController": false}, {"data": [0.45, 500, 1500, "POST - \/api (2)"], "isController": false}, {"data": [0.08333333333333333, 500, 1500, "POST - \/api (5)"], "isController": false}, {"data": [0.9, 500, 1500, "GET - \/"], "isController": false}, {"data": [0.0, 500, 1500, "POST - \/api (25)"], "isController": false}, {"data": [0.0, 500, 1500, "POST - \/api (200)"], "isController": false}, {"data": [0.0, 500, 1500, "POST - \/api (300)"], "isController": false}, {"data": [0.0, 500, 1500, "POST - \/api (100)"], "isController": false}, {"data": [0.0, 500, 1500, "POST - \/api (50)"], "isController": false}]}, function(index, item){
        switch(index){
            case 0:
                item = item.toFixed(3);
                break;
            case 1:
            case 2:
                item = formatDuration(item);
                break;
        }
        return item;
    }, [[0, 0]], 3);

    // Create statistics table
    createTable($("#statisticsTable"), {"supportsControllersDiscrimination": true, "overall": {"data": ["Total", 1157, 0, 0.0, 785.000864304235, 293, 60797, 771.4000000000001, 1209.2999999999997, 13233.180000000042, 1.1032191687429498, 2.868953867894034, 0.19540580312829856], "isController": false}, "titles": ["Label", "#Samples", "KO", "Error %", "Average", "Min", "Max", "90th pct", "95th pct", "99th pct", "Transactions\/s", "Received", "Sent"], "items": [{"data": ["POST - \/api (1)", 30, 0, 0.0, 650.9666666666668, 507, 1136, 866.8, 1005.6499999999999, 1136.0, 2.7462467960454044, 0.7536087399304284, 0.6972892255584036], "isController": false}, {"data": ["POST - \/api (10)", 3, 0, 0.0, 3386.3333333333335, 2718, 3890, 3890.0, 3890.0, 3890.0, 0.295217476874631, 0.08101182715016729, 0.07524586080495965], "isController": false}, {"data": ["POST - \/api (20)", 3, 0, 0.0, 6051.0, 5888, 6224, 6224.0, 6224.0, 6224.0, 0.16523463317911433, 0.04534270695637806, 0.04211546802709848], "isController": false}, {"data": ["POST - \/api (2)", 100, 0, 0.0, 1061.42, 605, 2202, 1508.5000000000002, 1667.3999999999994, 2201.24, 5.199396869963085, 1.426787617636354, 1.3201593615140643], "isController": false}, {"data": ["POST - \/api (5)", 6, 0, 0.0, 1887.1666666666667, 1350, 2707, 2707.0, 2707.0, 2707.0, 0.6851661527920521, 0.18801922747516273, 0.173967968482357], "isController": false}, {"data": ["GET - \/", 1000, 0, 0.0, 358.9860000000001, 293, 865, 594.0, 598.0, 608.0, 74.18947993174568, 220.02592864363083, 12.244162215297871], "isController": false}, {"data": ["POST - \/api (25)", 3, 0, 0.0, 7343.666666666667, 6788, 8294, 8294.0, 8294.0, 8294.0, 0.13616557734204796, 0.03736574925108933, 0.03470626531862745], "isController": false}, {"data": ["POST - \/api (200)", 1, 0, 0.0, 46738.0, 46738, 46738, 46738.0, 46738.0, 46738.0, 0.021395866318627242, 0.005871326597201421, 0.005474332983867517], "isController": false}, {"data": ["POST - \/api (300)", 3, 0, 0.0, 50054.666666666664, 43339, 60797, 60797.0, 60797.0, 60797.0, 0.019977092933436327, 0.005481995228804304, 0.005111326512265935], "isController": false}, {"data": ["POST - \/api (100)", 5, 0, 0.0, 24907.4, 22792, 26412, 26412.0, 26412.0, 26412.0, 0.028882034219434142, 0.00792563634341894, 0.007389739224113033], "isController": false}, {"data": ["POST - \/api (50)", 3, 0, 0.0, 13494.666666666666, 12990, 13925, 13925.0, 13925.0, 13925.0, 0.07409785857188728, 0.020333494393262035, 0.01888627059302986], "isController": false}]}, function(index, item){
        switch(index){
            // Errors pct
            case 3:
                item = item.toFixed(2) + '%';
                break;
            // Mean
            case 4:
            // Mean
            case 7:
            // Percentile 1
            case 8:
            // Percentile 2
            case 9:
            // Percentile 3
            case 10:
            // Throughput
            case 11:
            // Kbytes/s
            case 12:
            // Sent Kbytes/s
                item = item.toFixed(2);
                break;
        }
        return item;
    }, [[0, 0]], 0, summaryTableHeader);

    // Create error table
    createTable($("#errorsTable"), {"supportsControllersDiscrimination": false, "titles": ["Type of error", "Number of errors", "% in errors", "% in all samples"], "items": []}, function(index, item){
        switch(index){
            case 2:
            case 3:
                item = item.toFixed(2) + '%';
                break;
        }
        return item;
    }, [[1, 1]]);

        // Create top5 errors by sampler
    createTable($("#top5ErrorsBySamplerTable"), {"supportsControllersDiscrimination": false, "overall": {"data": ["Total", 1157, 0, null, null, null, null, null, null, null, null, null, null], "isController": false}, "titles": ["Sample", "#Samples", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors"], "items": [{"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}]}, function(index, item){
        return item;
    }, [[0, 0]], 0);

});
