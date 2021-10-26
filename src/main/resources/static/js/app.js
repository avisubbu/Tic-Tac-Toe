(function(){
    $(document).ready(function(){
        enterRoom();
    });
})(jQuery);

function enterRoom(){
    var gameType=$("#gameType").val();
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/game/"+gameType+"/"+$('#name').val(),
        timeout: 100000,
        success:function(data){
            console.log("SUCCESS: "+data);
            $('#gameId').val(data);
            $('.starter-template').removeClass('hide');
            $('table.gamepanel').find('tr').each(function(i,j){
                $(j).find('Button.YTF').each(function(k,l){
                    $(l).click(function(){
                        gameAction($(this).attr('row'),
                            $(this).attr('col'));
                    });
                    $(l).attr('row',i);
                    $(l).attr('col',k);
                });
            });
        },
        error:function(e){
            console.log("ERROR: ", e);
        },
        done:function(e){
            console.log("DONE");
        }
    });
}

function gameAction(row, col){
    var data = {};
    data["gameId"]=$('#gameId').val();
    data["row"] = row;
    data["col"] = col;
    data["player"] = $('#name').val();
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/move",
        timeout: 100000,
        data: JSON.stringify(data),
        dataType: "json",
        success:function(data){
            console.log("SUCCESS: "+data);
            $('table.gamepanel').find('tr').each(function(i,j){
                $(j).find('Button.YTF').each(function(k,l){
                    if(data[i][k]!='_'){
                        $(l).attr("disabled",true);
                        $(l).text(data[i][k]);
                    }
                });
            });
            gameResult();
            gameSteps();
        },
        error:function(e){
            console.log("ERROR: ", e);
        },
        done:function(e){
            console.log("DONE");
        }
    });
}

function gameSteps(){
    var gameId=$("#gameId").val();
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/step/"+gameId,
        timeout: 100000,
        success:function(data){
            console.log("SUCCESS: "+data);
            $('#steps').find('tr').remove();
            $.each(data, function(i,j){
                if('Computer'==j.player){
                    $('#steps').append('<tr><td>Computer - </td><td>row:'+j.step[0]+', column:'+j.step[1]+'</td></tr>');
                }else{
                    $('#steps').append('<tr><td>You - </td><td>row:'+j.step[0]+', column:'+j.step[1]+'</td></tr>');
                }
            });
        },
        error:function(e){
            console.log("ERROR: ", e);
        },
        done:function(e){
            console.log("DONE");
        }
    });
}

function gameResult(){
    var gameId=$("#gameId").val();
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/result/"+gameId+"/"+$('#name').val(),
        timeout: 100000,
        success:function(data){
            console.log("SUCCESS: "+data);
            if(data != null && data != ''){
                $('#result').text(data);
                $('Button.YTF').each(function(i,j){
                    $(j).attr('disabled', true);
                });
            }
        },
        error:function(e){
            console.log("ERROR: ", e);
        },
        done:function(e){
            console.log("DONE");
        }
    });
}