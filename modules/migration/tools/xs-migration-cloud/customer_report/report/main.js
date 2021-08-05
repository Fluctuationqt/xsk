function spaces(e){for(var t="",a=0;a<e;a++)t+=space;return t}function createSection(e,t,a,n){var i=$(generateTag("div",{id:t})).appendTo(e);i.append(generateTag(Constant.title_m,{text:a})),n&&i.append(generateTag("p",{text:n}))}function createTabs(e,t,a,n,i){$(e).append(generateTag("div",{id:t})),createTabItems("#"+t,a,n,i),$("#"+t).tabs({activate:function(e,t){t.newPanel.attr("id").indexOf("FilesTab")>=0&&$("#"+t.newPanel.attr("id")).css("display","inline-block")}})}function createDialog(e,t,a,n){0==$("#"+t).length&&$(e).append(generateTag("div",{id:t,properties:[{name:"title",value:a.title}]})),$("#"+t).append(generateTag("p",{text:generateTag("span",{properties:[{name:"class",value:"ui-icon ui-icon-alert"},{name:"style",value:"float:left; margin:0 7px 20px 0;"}]})+a.content})),$(function(){$("#"+t).dialog({modal:!0,width:a.width,buttons:{Ok:function(){$(this).dialog("close")}}})})}function createTabItems(e,t,a,n){$(e).append(generateTag("ul"));for(var i=0;i<t.length;i++)$(generateTag("li")).appendTo(e+" > ul").append(generateTag("a",{text:t[i],properties:[{name:"href",value:"#"+a[i]}]})),n&&n.contents?$(e).append(n.contents[i]):$(generateTag("div",{id:a[i]})).appendTo(e)}function createStepsArea(e,t,a,n){0==$("#"+t).length&&$(e).append(generateTag("div",{id:t,properties:[{name:"style",value:"display: inline-block;"}]})),createStepItems("#"+t,a,n)}function createMigrationStepSection(e,t,a,n){0==$("#"+t).length&&$(e).append(generateTag("div",{id:t})),$("#"+t).css("border-style","solid"),$("#"+t).css("padding-left","40px"),$("#"+t).css("padding-right","40px"),$("#"+t).css("border-width","1px"),$("#"+t).css("border-radius","8px"),a.link.text="more information",createDescriptionArea("#"+t,t+"_desc",a.desc,a.link,n),createMigrationStepListArea("#"+t,t+"_detail",a.list,n)}function createDescriptionArea(e,t,a,n,i){0==$("#"+t).length&&$(e).append(generateTag("div",{id:t}));var r=a;$("#"+t).append(generateTag("p",{text:r}))}function createMigrationStepListArea(e,t,a,n){0==$("#"+t).length&&$(e).append(generateTag("div",{id:t}));for(var i=[],r=[],l=0;l<a.length;l++)i.push(a[l].text),r.push(a[l].value);var o=getStepInfoText(i,r,n)+generateTag("a",{id:t+"listlink",text:"Detail List",properties:[{name:"href",value:window.location.protocol+"//"+window.location.hostname+(""!=window.location.hostname?":":"")+window.location.port+(""!=window.location.port?"/":"")+window.location.pathname+"?view=detail&step="+n.step},{name:"target",value:"_blank"}]});$("#"+t).append(generateTag("p",{text:o}))}function getStepInfoText(e,t,a){for(var n="",i=0;i<e.length;i++){0!=i&&(n+=",&nbsp;&nbsp;&nbsp;&nbsp;");var r=e[i].toLowerCase()+(t[i]>1?"s":"");n+=t[i]+"&nbsp;"+r}return""!=n&&(n+="&nbsp;&nbsp;&nbsp;&nbsp;"),n}function createStepItems(e,t,a){var n=null;if(t&&t.steps){n=t.steps;t.stepContentIds;for(var i=0;i<n.length;i++){var r=n[i];r.id=e.slice(1,e.length)+"_step"+i,$(e).append(generateTag(Constant.title_s,{text:"Step "+(i+1)+" -  "+r.name,id:r.id+"_title"})),$(generateTag("div",{id:r.id})).appendTo(e),createMigrationStepSection("#"+r.id,r.id+"_section",r,{step:i})}}}function createFormPanel(e,t,a,n,i,r){if(0==$("#"+t).length&&$(e).append(generateTag("div",{id:t})),r&&r.style)for(l=0;l<r.style.length;l++)$("#"+t).css(r.style[l].name,r.style[l].value);$(generateTag("table",{id:t+"_table",properties:[]})).appendTo("#"+t);if(r&&r.table_style)for(l=0;l<r.table_style.length;l++)$("#"+t+"_table").css(r.table_style[l].name,r.table_style[l].value);$(generateTag("col",{properties:[{name:"width",value:r&&r.width?r.width:"140px"}]})).appendTo("#"+t+"_table");for(var l=0;l<n.length;l++){var o=$(generateTag("tr")).appendTo("#"+t+"_table");o.append(generateTag("td",{text:n[l]+(""==n[l].trim()||n[l].trim()==space?"":":")})),o.append(generateTag("td",{text:i[l]}))}}function createProjectInfoArea(e,t,a,n){0==$("#"+t).length&&$(e).append(generateTag("div",{id:t})),createFormPanel("#"+t,t+"_form",4,a.names,a.values,n)}function createTitle(e,t,a,n){$(e).append("<div id = '"+t+"'></div>");var i="";n&&n.icon&&(i+="<img src='"+n.icon+(n.icon_width?"' width='"+n.icon_width:"")+(n.icon_height?"' height='"+n.icon_height:"")+"'/>");var r="h1";n&&n.size&&(r=n.size);var l=generateTag(r,{text:i+a});n&&n.addText&&(l+=n.addText),$("#"+t).append(l)}function createListArea(e,t,a){return 0==$("#"+t).length&&$(e).append(generateTag("div",{id:t})),$("#"+t).append(generateTag("table",{id:t+"_t"})),{header:$(generateTag("thead",{id:t+"_t_hd",properties:[{name:"class",value:"ui-widget-header"}]})).appendTo("#"+t+"_t"),body:$(generateTag("tbody",{id:t+"_t_bd",properties:[{name:"class",value:"ui-widget-content"}]})).appendTo("#"+t+"_t")}}function appendColumes(e,t){for(var a=$(generateTag("tr")).appendTo(e),n=0;n<t.length;n++)a.append(generateTag("th",{text:t[n]}))}function appendRecord(e,t,a){for(var n=0;n<t.length;n++)for(var i=t[n],r=$(generateTag("tr",{properties:[{name:"class",value:"ui-widget-content"}]})).appendTo(e),l=0;l<a.length;l++){var o=a[l];r.append(generateTag("td",{text:i[o]}))}}function generateTag(e,t){var a="";if(a+="<"+e,t&&t.id&&(a+=" id= '"+t.id+"'"),t&&t.properties)for(var n=0;n<t.properties.length;n++)a+=" "+t.properties[n].name+"= '"+t.properties[n].value+"' ";return a+=">",t&&t.text&&(a+=t.text),a+="</"+e+">"}function load_local_file(e,t){$.ajax({url:e.replace('/"/g',""),dataType:"text"}).done(function(e){t&&t(e)}).fail(function(t,a,n){alert("Unable to read file "+e+": "+n)})}function enableOpenHighlighter(e,t){t&&$("."+e).unbind("click"),$("."+e).click(function(){var e=this;$(this).attr("highlight");load_local_file(e.text.replace(/\\/g,"/"),function(t){var a="";a="<plaintext>"+t;window.target="_blank";var n=window.open("report/content.html");n.filename=e.text,window.navigator.userAgent.indexOf("MSIE ")>0||navigator.userAgent.match(/Trident.*rv\:11\./)?setTimeout(function(){checkReadyState(function(){n.document.getElementById("codecontent").innerHTML=a,n.focus()},n)},1e3):$(n).on("load",function(){n.document.getElementById("codecontent").innerHTML=a,n.focus()})})})}function checkReadyState(e,t){"complete"!==t.document.readyState?setTimeout(function(){checkReadyState(e,t)},100):e()}function generateFileListObject(){if(!main_filelist)return null;for(var e={sum:[{name:"total generated files",value:main_filelist.length}],detail:[{name:"xsjs",number:0,detail:{}},{name:"db",number:0,detail:{}},{name:"web",number:0,detail:{}},{name:"todo",number:0,detail:{}}]},t=0;t<main_filelist.length;t++){for(var a=main_filelist[t],n=!1,i=0;i<e.detail.length;i++){var r=e.detail[i];if(a.container==r.name){n=!0,r.number++,r.detail["."+a.ext]?r.detail["."+a.ext]++:r.detail["."+a.ext]=1;break}}if(!1===n){var l={name:a.container,number:1,detail:{}};l.detail["."+a.ext]=1,e.detail.push(l)}}for(var o=[],t=0;t<e.detail.length;t++)e.detail[t].number>0&&o.push('"'+e.detail[t].name+'"');return e.sum.push({name:"content containers",value:o.toString()}),e}function initDetailView(e,t,a,n){0==$("#"+t).length&&$(e).append(generateTag("div",{id:t,properties:[{name:"class",value:"ui-widget-content"}]})),createDetailContent("#"+t,t+"_content",a,n)}function initDetailView_standalone(e,t,a,n){0==$("#"+t).length&&$(e).append(generateTag("div",{id:t,properties:[{name:"class",value:"ui-widget-content"}]})),createDetailContent_standalone("#"+t,t+"_content",a,n)}function createDetailContent_standalone(e,t,a,n){0==$("#"+t).length&&$(e).append(generateTag("div",{id:t,properties:[{name:"class",value:"ui-widget-content"}]}));var i=null,r=null,l=a.steps[n.step];i="Step "+(parseInt(n.step)+1)+": "+l.name,r=t+"_tabs",$("#"+t).append(generateTag(t+"_tabs")),n||(n={}),n.desc=l.desc,n.standalone=!0,createDetailSection(t+"_tabs",i,r,l,n),enableOpenHighlighter("FileOpen",!0)}function createDetailContent(e,t,a,n){0==$("#"+t).length&&$(e).append(generateTag("div",{id:t,properties:[{name:"class",value:"ui-widget-content"}]}));for(var i=[],r=[],l=0;l<a.steps.length;l++){var o=a.steps[l];i.push("Step "+(l+1)+": "+o.name),r.push(t+"_tabs_sections_"+l)}createDetailSectionTabs("#"+t,t+"_tabs",i,r,n);for(l=0;l<a.steps.length;l++)createDetailSection(t+"_tabs",i[l],r[l],a.steps[l],n);enableOpenHighlighter("FileOpen",!0)}function createDetailSectionTabs(e,t,a,n,i){$(function(){$("#"+t).addClass("ui-tabs-vertical ui-helper-clearfix"),$("#"+t+" li").removeClass("ui-corner-top").addClass("ui-corner-left")}),createTabs(e,t,a,n,i)}function createDetailSection(e,t,a,n,i){i&&1==i.standalone&&0==Object.keys(n.messages).length&&(window.location.href=n.link.url),0==$("#"+a).length&&$(e).append(generateTag("div",{id:a})),generateDetailFromMessages("#"+a,t,a+"_content",n.messages,i)}function generateDetailFromMessages(e,t,a,n,i){if(0==$("#"+a).length&&$(e).append(generateTag("div",{id:a})),$("#"+a).append(generateTag(Constant.title_m,{text:t})),$("#"+a).append(generateTag("p",{text:i&&i.desc?i.desc:"description area"})),i||(i={}),i.style={name:"style",value:"padding-left:20px"},n.ERROR&&(createDetailSectionTitle(e,a+"_title","Error ("+n.ERROR.length+")",i),generateDetailTable(e,a+"table_error",["type","category","file"],r=getDetailTableMessages(n.ERROR),["type","category","file"],i)),n.WARNING&&(createDetailSectionTitle(e,a+"_title","Warning ("+n.WARNING.length+")",i),generateDetailTable(e,a+"table_warning",["type","category","file"],r=getDetailTableMessages(n.WARNING),["type","category","file"],i)),n.INFO){createDetailSectionTitle(e,a+"_title","Info ("+n.INFO.length+")",i);var r=getDetailTableMessages(n.INFO);generateDetailTable(e,a+"table_info",["type","category","file"],r,["type","category","file"],i)}}function getDetailTableMessages(e){for(var t=[],a=0;a<e.length;a++){var n=e[a],i=null;if(n.message&&"[object Array]"===Object.prototype.toString.call(n.message)&&n.message.length>1){i=n.message[0];for(var r=0;r<n.message.length-1;r++){var l="{*"+r+"}",o=RegExp(l,"g");i=i.replace(o,n.message[r+1])}}else i=n.message.toString();n.description&&(i+="<br>"+n.description.toString());var s="",c="";if(n.loc){if(n.loc.length>5&&(s+="<br>"),s+=" (in line: ","[object Array]"===Object.prototype.toString.call(n.loc))for(var p in n.loc)s+=n.loc[p].start.line,c+=n.loc[p].start.line,p!=n.loc.length-1&&(s+=", ",c+=", ");else s+=n.loc.start.line,c+=n.loc.start.line;s+=") "}var d=n.file&&""!=n.file?"<a class='FileOpen' highlight = '"+c+"'><u>"+n.file+"</u></a> "+s+"<br>":"";t.push({type:n.type,category:n.category,file:d+i})}return t}function generateDetailTable(e,t,a,n,i,r){detail_area=createListArea(e,t,r),detail_area.header.parent().css("padding-left","8px"),detail_area.header.parent().css("padding-right","40px"),appendColumes(detail_area.header,a),appendRecord(detail_area.body,n,i)}function createDetailSectionTitle(e,t,a,n){var i={text:a};i.properties=[],n&&n.style&&i.properties.push(n.style);var r=generateTag("u",i);$(e).append(generateTag(Constant.title_m,{text:r}))}function createDetailSectionTable(e,t,a,n){}function setSectionBorderStyle(e){$("#"+e).css("padding-left","8px"),$("#"+e).css("padding-right","40px")}function initFileListView(e,t,a){statisticObject||(statisticObject=generateFileListObject()),0==$("#"+t).length&&$(e).append(generateTag("div",{id:t,properties:[{name:"class",value:"ui-widget-content"}]})),createFilterArea("#"+t,t+"_filters",statisticObject.detail,a),$("#"+t).append("<hr>"),createFileListContent("#"+t,t+"_content",a)}function createFilterArea(e,t,a,n){0==$("#"+t).length&&$(e).append(generateTag("div",{id:t})),n&&n.newtab&&1==n.newtab&&($("#"+t).css("padding-left","8px"),$("#"+t).css("padding-right","40px"));var i=[{text:"All",key:"all"}];filterExt=[{text:"All",key:"all"}];for(var r=0;r<a.length;r++){i.push({text:a[r].name,key:a[r].name});for(var l in a[r].detail)filterExt.push({text:l,key:l,container:a[r].name})}var o={text:"filter for container: "};o.change=function(){containerFilterUpdate(t)};var s={text:"filter for extension: "};s.change=function(){extensionFilterUpdate(t)},createFilter("#"+t,t+"_con",i,o),createFilter("#"+t,t+"_ext",filterExt,s)}function containerFilterUpdate(e){var t=[],a=$("#"+e+"_con_section_list option:selected");if(1==a.length){if(fileFilter.container=a.attr("key"),"all"!=fileFilter.container){t=[{text:"All",key:"all"}];for(var n=0;n<filterExt.length;n++)filterExt[n].container==fileFilter.container&&t.push(filterExt[n])}else t=filterExt;fileFilter.extension="all",list_area.body.empty(),$("#"+e+"_ext_section").empty(),$("#"+e+"_ext_section").remove();var i={text:"filter for extension: "};i.change=function(){extensionFilterUpdate(e)},createFilter("#"+e,e+"_ext",t,i),appendCreatedFileList(list_area.body,main_filelist,fileFilter)}}function extensionFilterUpdate(e){var t=$("#"+e+"_ext_section_list option:selected");1==t.length&&(fileFilter.extension=t.attr("key"),list_area.body.empty(),appendCreatedFileList(list_area.body,main_filelist,fileFilter))}function createFilter(e,t,a,n){var i=$(generateTag("p",{id:t+"_section"})).appendTo(e);i.append(generateTag("lable",{text:n.text,properties:[{name:"for",value:t+"_section_list"}]})),i.append(generateTag("select",{id:t+"_section_list",properties:[{name:"name",value:t+"_section_list"}]}));for(var r=0;r<a.length;r++)$("#"+t+"_section_list").append(generateTag("option",{text:a[r].text,properties:[{name:"key",value:a[r].key}]}));$("#"+t+"_section_list").change(n.change)}function createFileListContent(e,t,a){list_area=createListArea(e,t,a),a&&a.newtab&&1==a.newtab&&(list_area.header.parent().css("padding-left","8px"),list_area.header.parent().css("padding-right","40px"));var n={container:"all",extension:"all"};a&&a.filter&&(n=a.filter),appendColumes(list_area.header,filelist_view_columns),appendCreatedFileList(list_area.body,main_filelist,n)}function appendCreatedFileList(e,t,a){var n="all"==a.container?null:a.container,i="all"==a.extension?null:a.extension,r=!1;if(n&&i){for(l=0;l<filterExt.length;l++)if(filterExt[l].key==i){if(filterExt[l].container==n){r=!1;break}r=!0}r&&(i=null)}for(var l=0;l<t.length;l++){var o=t[l],s=!0;if(n&&t[l].container!=n&&(s=!1),i&&-1==t[l].name.indexOf(i,t[l].name.length-i.length)&&(s=!1),s){var c=$(generateTag("tr",{properties:[{name:"class",value:"ui-widget-content"}]})).appendTo(e);c.append(generateTag("td",{text:o.container}));var p=generateTag("a",{text:generateTag("u",{text:o.name}),properties:[{name:"class",value:"FileListView"}]});c.append(generateTag("td",{text:p}))}}enableOpenHighlighter("FileListView",!0)}function createFileListFilterArea(e,t,a,n){}function createFileListSearchArea(e,t,a,n){}function initSummaryView(e,t){var a={names:[],values:[]};a.names.push("Project"),a.values.push(main_data.project.name+" - "+main_data.project.version),a.names.push("Content");var n="";n+=main_data.task.dus.length+(main_data.task.dus.length>1?" DUs, ":" DU, ");for(var i=0;i<main_data.task.dus.length;i++)n+=main_data.task.dus[i].name+" ("+main_data.task.dus[i].vendor+") -"+main_data.task.dus[i].version;n+=", including "+main_data.task.packages.length+" packages and "+main_data.sum[0].number+" objects",a.values.push(n),a.names.push("System"),a.values.push(main_data.system.protocol+"://"+main_data.system.host+":"+main_data.system.port+", SID:"+main_data.system.sid+", version "+main_data.system.hana_version),a.names.push("HALM version"),a.values.push(main_data.system.halm_version),a.names.push("migration tool"),a.values.push(main_data["mig-tool-version"]),createProjectInfo("#"+t,"summary_projectInfo",a),createMigrationStep("#"+t,"summary_migrationSteps",main_data)}function createProjectInfo(e,t,a,n){createSection(e,t,"Project Information"),createProjectInfoArea(e,t,a,n)}function createMigrationStep(e,t,a,n){createSection(e,t,"Migration Steps",CON_TEXT.Migration_Desc),createStepsArea("#"+t,"migration_steps_accordion",a)}function createDocsArea(e,t,a,n){if(createSection(e,t,"How to find useful docs"),a.docs&&a.docs.link)for(var i=0;i<a.docs.link.length;i++)createReferenceLinksArea("#"+t,t+"_link",a.docs.link[i],n)}function initStatisticsView(e,t){statisticObject||(statisticObject=generateFileListObject()),createSummaryInfo("#"+t,"statistics_projectInfo",statisticObject)}function createSummaryInfo(e,t,a,n){createSection(e,t,"Generated Result"),createStatisticsSumInfo("#"+t,t+"_infopanel",a.sum,n);for(var i=0;i<statisticObject.detail.length;i++){var r=statisticObject.detail[i];createContainerInfo("#"+t,t+"_container"+i+"_"+r.name,r,n)}}function createStatisticsSumInfo(e,t,a,n){for(var i=[],r=[],l=0;l<a.length;l++)i.push(a[l].name),r.push(a[l].value);createFormPanel(e,t,null,i,r,{width:"180px"})}function createContainerInfo(e,t,a,n){var i=a.name;createSection(e,t,i+=spaces(2)+"("+a.number+space+"files)"),createContainerInfoDetail("#"+t,t+"_detail",a.detail,n)}function createContainerInfoDetail(e,t,a,n){var i=[],r=[],l=[],o=[],s=!0;for(var c in a)s?(s=!1,i.push(c),r.push(a[c])):(s=!0,l.push(c),o.push(a[c]));i.length>l.length&&(l.push("&nbsp;"),o.push("&nbsp;")),$(e).append(generateTag("div",{id:t})),createFormPanel(e,t,null,i,r,{style:[{name:"float",value:"left"}]}),createFormPanel(e,t+"_right",null,l,o,{table_style:[{name:"padding-left",value:"100px"}]})}function ifIE(){return!!(window.navigator.userAgent.indexOf("MSIE ")>0||navigator.userAgent.match(/Trident.*rv\:11\./))}function clearContent(e){$("#"+e).empty(),$("#"+e).remove()}function compatibilityCode(){"undefined"==typeof UserAgentInfo||window.addEventListener||(UserAgentInfo.strBrowser=1)}function start_the_fun(){compatibilityCode();var e="mainView",t=getQueryString();t.view&&"main"!=t.view?"detail"==t.view?(createTitle("#content","stepdetail_title",tool_name,{icon:"migration/report/icons/sap.gif",icon_width:"100px",icon_height:"50px"}),initDetailView_standalone("#content","stepdetail_body",main_data,{newtab:!0,step:t.step})):"filelist"==t.view&&(createTitle("#content","filelist_title",tool_name,{icon:"migration/report/icons/sap.gif",icon_width:"100px",icon_height:"50px"}),initFileListView("#content","filelist_body",{newtab:!0})):(createTitle("#content","mainview_title",tool_name,{icon:"migration/report/icons/sap.gif",icon_width:"100px",icon_height:"50px"}),createTabs("#content","mainview_tabs",["Summary","File statistics","File List","Steps Detail"],[e+"_summaryTab",e+"_statisticsTab",e+"_FilesTab",e+"_detailTab"]),initSummaryView("#mainview_tabs",e+"_summaryTab"),initStatisticsView("#mainview_tabs",e+"_statisticsTab"),initFileListView("#mainview_tabs",e+"_FilesTab",{newtab:!1}),initDetailView("#mainview_tabs",e+"_detailTab",main_data,{newtab:!1})),ifIE()&&createDialog("#content","_IE_NotSupportDlg",{title:"Browser Not Fully Supported",width:500,content:"We recomment to use another Web Browser. We still have some issues for showing all content correctly in IE. If you are checking only very generic information, it might not be a big issue. However if you would like to investigate issues, it is very unlikely to work with IE."},null)}function getQueryString(){for(var e={},t=window.location.search.substring(1).split("&"),a=0;a<t.length;a++){var n=t[a].split("="),i=n[0],r=n[1],l=e[i];void 0===l?e[i]=decodeURIComponent(r):"string"==typeof l?e[i]=[e[i],decodeURIComponent(r)]:Array.isArray(l)&&e[i].push(decodeURIComponent(r))}return e}var space="&nbsp;",CON_TEXT={Migration_Desc:"The objects from the provided delivery units have been exported from the system, analyzed, migrated, and have been written into an XS Advanced folder structure. Follow the steps shown below in order to complete the migration."},Constant={title_l:"h3",title_m:"h4",title_s:"h5"},filelist_view_columns=["container","file name"],fileFilter={container:"all",extension:"all"},filter_container={all:!0,xsjs:!1,web:!1,db:!1,todo:!1,delete:!1},filterExt=[],list_area=null,statisticObject=null,Views={main:"main",detail:"detail",list:"filelist",statistics:"statistics"},tool_name="XS Migration Report "+main_data.project.name;