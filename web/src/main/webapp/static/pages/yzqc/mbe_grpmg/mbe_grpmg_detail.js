define(['jquery', 'knockout', 'text!static/pages/yzqc/mbe_grpmg/mbe_grpmg_detail.html'],
    function ($, ko, template) {
        var viewModel = {
            data: ko.observable({})
        }
        viewModel.submitForm = function () {
            var flag = $("#form").validate().check();
            if (!flag) {
                return;
            }

//			debugger;
            var saveUrl = $ctx + '/mbe_grpmg/create.json';
            if (viewModel.data().pkMbeGroupmg != null) {
                saveUrl = $ctx + '/mbe_grpmg/edit.json';
            }
            console.info(saveUrl);
            $.ajax({
                type: 'POST',
                contentType: 'application/json',
                url: saveUrl,
                data: JSON.stringify(viewModel.data()),
                dataType: 'json',
                success: function (data) {
                    if (data != null) {
                        jAlert("保存成功!", "提示");
                        window.location.href = "#/yzqc/mbe_grpmg/mbe_grpmg_list";
                    }
                },
                error: function (resp, textStatus, errorThrown) {
                    //数据被删除，跳转到列表页
                    if (resp.status == '404') {
                        if (confirm(resp.responseText, "错误")) {
                            window.location.href = "#/yzqc/mbe_grpmg/mbe_grpmg_list";
                        }
                        return;
                    }

                    //数据已经被修改，重新加载
                    if (resp.status == '409') {
                        if (confirm(resp.responseText, "错误")) {
                            loadData(viewModel.data().pkMbeGroupmg);
                        }
                        return;
                    }

                    if (resp.status == '500') {
                        var option = {
                            type: 'danger',//success info warning danger
                            msg: "保存出错",
                            pos: {top: 50, right: 10}
                        };
                        $.showMessage(option);
                        return;
                    }

                    if (resp.responseJSON) {
                        var validateObj = resp.responseJSON;
                        var msg = "";
                        if (validateObj) {
                            for (var key in validateObj) {
                                if (key == '__proto__') {
                                    continue;
                                }
                                msg += validateObj[key] + "<br/>";
                            }
                        }
                        var option = {
                            type: 'danger',//success info warning danger
                            msg: msg == '' ? "保存出错" : msg,
                            pos: {top: 50, right: 10}
                        };
                        $.showMessage(option);
                    }
                }
            });
        }

        var loadData = function (id) {
//			debugger;
            var infoUrl = $ctx + '/mbe_grpmg/create.json';
            if (id && id != 0) {
                infoUrl = $ctx + '/mbe_grpmg/edit/' + id + ".json";
            }
            $.ajax({
                type: 'GET',
                url: infoUrl,
                dataType: 'json',
                success: function (data) {
                    viewModel.data(data);
                },
                error: function (resp, textStatus, errorThrown) {
                    if (confirm(resp.responseText, "错误")) {
                        window.location.href = "#/yzqc/mbe_grpmg/mbe_grpmg_list";
                    }
                }
            });
        }

        var init = function (params) {
            loadData(params[0]);
        }

        return {
            'model': viewModel,
            'template': template,
            'init': init
        };
    }
);