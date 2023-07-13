var dg = $(".dg");
var windowHeight;

$(document).ready(function() {
  dg.datagrid({
    view: detailview,
    detailFormatter:function(index,row){
      return '<div class="ddv" style="padding:5px 0"></div>';
    },
    onExpandRow: function(index,row){
      var ddv = $(this).datagrid('getRowDetail',index).find('div.ddv');
      ddv.panel({
        border:false,
        cache:false,
        href:'/clientes/get_casas/'+row.id,
        onLoad:function(){
          dg.datagrid('fixDetailRowHeight',index);
        }
      });
      dg.datagrid('fixDetailRowHeight',index);
    }
  });
  dg.datagrid("enableFilter");
});
