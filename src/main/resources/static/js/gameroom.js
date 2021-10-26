var ws = null;

function connect(){
    ws = new WebSocket('ws://'+window.location.host+'/game/room');
    ws.onopen=function(event){
        receive();
        ws.send("ESTABLISH_CONNECTION");
    }
}

function receive(){
    ws.onmessage=function(event){
        console.log("Received: "+event.data);
        var dataObj = JSON.parse(event.data);
        if("YOUR_TURN"==dataObj.status){
            gameResult();
            gameSteps();
            $('#gameId').val(dataObj.gameId);
            $('#playerInfo').addClass('hide');
            $('.starter-template').removeClass('hide');
            gameBoard();
        }else if("START_GAME"==dataObj.status){
            $('#gameId').val(dataObj.gameId);
            $('#name').val(dataObj.player1);
            $('#playerInfo').addClass('hide');
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
        }else if("START_GAME_NOT_YOUR_TURN"==dataObj.status){
            $('#gameId').val(dataObj.gameId);
            $('#name').val(dataObj.player2);
            $('#playerInfo').addClass('hide');
            $('.starter-template').removeClass('hide');
            $('table.gamepanel').find('tr').each(function(i,j){
                $(j).find('Button.YTF').each(function(k,l){
                    $(l).click(function(){
                        gameAction($(this).attr('row'),
                            $(this).attr('col'));
                    });
                    $(l).attr('disabled',true);
                    $(l).attr('row',i);
                    $(l).attr('col',k);
                });
            });
        }
    }
}

function gameBoard(){
    var gameId=$('#gameId').val();

    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/board/"+gameId,
        timeout: 100000,
        success:function(data){
            console.log("SUCCESS: "+data);
            $('table.gamepanel').find('tr').each(function(i,j){
                $(j).find('Button.YTF').each(function(k,l){
                    if(data[i][k]!='_'){
                        $(l).attr("disabled",true);
                        $(l).text(data[i][k]);
                    }else{
                        $(l).removeAttr('disabled');
                        $(l).attr('row',i);
                        $(l).attr('col',k);
                    }
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
                        $(l).attr("disabled",true);
                        $(l).text(data[i][k]);
                });
            });
            gameResult();
            gameSteps();
            ws.send('MOVE');
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
    var gameId=$("#gameId").val(),
        player=$('#name').val();
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/result/"+gameId+"/"+player,
        timeout: 100000,
        success:function(data){
            console.log("SUCCESS: "+data);
            if(data != null && data != ''){
                $('#result').text(data);
                $('Button.YTF').each(function(i,j){
                    $(j).attr('disabled', true);
                });
                ws.close();
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
                if($('#name').val()==j.player){
                    $('#steps').append('<tr><td>You - </td><td>row:'+j.step[0]+', column:'+j.step[1]+'</td></tr>');
                }else{
                    $('#steps').append('<tr><td>Opponent - </td><td>row:'+j.step[0]+', column:'+j.step[1]+'</td></tr>');
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

$(function(){
    $(document).ready(function(){
        connect();
    });
});