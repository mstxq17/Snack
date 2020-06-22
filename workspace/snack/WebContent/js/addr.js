window.onload=function(){
	var pro=document.getElementById("province");
	var city=document.getElementById("city");
	var area=document.getElementById("area");
	
	var dom;
	var xmlhttp=new XMLHttpRequest();
	xmlhttp.onreadystatechange=function(){
		if(xmlhttp.readyState==4){
			if(xmlhttp.status==200){
				dom=xmlhttp.responseXML.documentElement;
			
				//获取所有的省份
				var pros=dom.getElementsByTagName("province");
				
				var len=pros.length;
				for(var i=0;i<len;i++){
					addOption(pros.item(i),pro);
				}
				
				var cities;
				var citylen;
				
				//当省份发生改变的时候，获取当前省份下的所有城市
				pro.onchange=function(){
					var flag=pro.value; //获取当前选中的省份的编号postcode
					
					//清空城市下拉列表中的值
					city.length=0;
					
					for(var i=0;i<len;i++){
						if(pros[i].nodeType==1 && pros[i].getAttribute("name")==flag){
							//取出该省份下的所有城市
							cities=pros[i].childNodes;
							citylen=cities.length;
							for(var j=0;j<citylen;j++){
								addOption(cities[j],city);
							}
							
							//出发城市的onchange事件
							city.onchange();
							break;
						}
					}
				}
				
				//当城市发生改变的时候
				city.onchange=function(){
					var index=city.value; //获取当前选中的城市的编号postcode
					
					//清空地区
					area.length=0;
					
					for(var i=0;i<citylen;i++){
						if(cities[i].nodeType==1 && cities[i].getAttribute("name")==index){
							var areas=cities[i].childNodes;
							var arealen=cities.length;
							for(var j=0;j<arealen;j++){
								addOption(areas[j],area);
							}
							break;
						}
					}
				}
			}
		}
	}
	xmlhttp.open("GET","../xml/city.xml",true); //异步
	xmlhttp.send(null);
}

function addOption(node,obj){
	if(node!=undefined && node.nodeType==1){
		var opt=document.createElement("option");
		opt.setAttribute("value",node.getAttribute("name"));
		opt.appendChild( document.createTextNode( node.getAttribute("name")  ) );
		obj.appendChild(opt);
	}
}