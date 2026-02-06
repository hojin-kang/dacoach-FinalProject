function kakaopayModule(order_id,user_id,item_name,quantity,total_amount,vat_amount,tax_free,url){
	var parameters = {
			cid:"TC0ONETIME",
			partner_order_id:order_id,
			partner_user_id:user_id,
			item_name:item_name,
			quantity:quantity,
			total_amount:total_amount,
			vat_amount:vat_amount,
			tax_free_amount:tax_free,
			approval_url:"http://localhost:9090"+url+"/success",
				fail_url:"http://localhost:9090"+url+"/fail",
				cancel_url:"http://localhost:9090"+url+"/cancel"};
fetch(url+'/ready',
		{method:'POST',
		headers:{'Content-Type':'application/json'},
		body:JSON.stringify(parameters)})
		
.then(function(resp){
	if(resp.ok){
		return resp.text();
	}else{
		throw new Error('예기치못한 예러가 발생했습니다. 고객센터에 문의 해주세요');
	}
	
})

.then(function(data){
	var val=JSON.parse(data);
	window.location.href = val.next_redirect_pc_url;
})

.catch(function(error){
	alert(error.message);
});
}