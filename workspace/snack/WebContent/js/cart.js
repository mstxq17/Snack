// JavaScript Document
//商品数量的添加
function add(obj, cno){	
	//获取购物车中点击的商品数量
	var num = $(obj).prev().val();
	num++;
	
	//数量写入标签中
	$(obj).prev().val(num);	
	//获取单价
	var price = $(obj).parent().parent().prev().html();
	//获取小计
	var total = num*price*1.00;
	//获取小计标签
	var $subtotal = $(obj).parent().parent().next();
	//小计价钱写入标签中
	$subtotal.text(total.toFixed(2));
	
	productCount();
}

//商品数量的减法
function lost(obj, cno){
	//获取购物车中点击的商品数量
	var num = $(obj).next().val();
	//判断此商品的数量是否大于1
	if( num <= 1){
		return; 
	}
	num--;
	
	//数量写入标签中
	$(obj).next().val(num);	
	//获取单价
	var price = $(obj).parent().parent().prev().html();
	//获取小计
	var total = num*price*1.00;
	//获取小计标签
	var $subtotal = $(obj).parent().parent().next();
	
	$subtotal.text(total.toFixed(2));
	
	productCount();
}

//删除商品
function delGoods(obj, cno){
	//温馨提示
	var result = confirm("您确定要删除购物车中当前商品吗？");
	if (!result) {
		return;
	}
	$(obj).parent().parent().remove();
	productCount();
}

//全选和全不选
$("#all").click(function(){
	//获取全选是否被选中
	//prop 获取标签的固有属性  attr 自动义的属性
	var flag = $(this).prop("checked");
	//判断    
	$(".cart_list_td ul .col01 input").prop("checked",flag);
	
	productCount();
});


$(function(){
	bindEvent();
	productCount();
})

function bindEvent() {
	var checkboxs = $("#cart_list input[type='checkbox']");
	var len = checkboxs.length;
	for (var i = 0; i < len; i++) {
		checkboxs[i].onclick = function() {
			productCount();
			
			for (var j = 0; j < len; j++) {
				if (!checkboxs[j].checked) {
					$("#all").prop("checked", false);
					return;
				}
			}
			$("#all").prop("checked", true);
		}
	}
}

//计算商品总额的方法
function productCount(){
	var total=0;  //总价
	var price;//每一行的市场价
	var number;//每一行的数量
	var numbers=0;//总数量
	var myul=$(".cart_list_td ul");

	for(var i=0;i<myul.length;i++){//循环每一行
		if ($(myul[i]).find("input[type='checkbox']").prop("checked") ){
			price=$(".cart_list_td  ul:eq("+i+")").find(".col05").html();
			number=$(".cart_list_td  ul:eq("+i+")").find(".col06 input").val();
			total+=price*number;
			numbers+= number*1.0;
		}
	}
	$("#totalPrices").text(total.toFixed(2));
	$("#totalNumbers").text(numbers);
	$("#iconfont").text(numbers);
	$("#totalcount").text(numbers);
}


