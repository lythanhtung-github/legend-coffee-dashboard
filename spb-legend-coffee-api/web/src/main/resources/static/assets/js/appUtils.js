class AppUtils {

    static DOMAIN_SERVER = "http://localhost:25001";

    static STAFF_API = this.DOMAIN_SERVER + "/api/staffs";

    static USER_API = this.DOMAIN_SERVER + "/api/users";

    static AUTH_URL = this.DOMAIN_SERVER + "/api/auth";

    static ROLE_API = this.DOMAIN_SERVER + "/api/roles";

    static PRODUCT_API = this.DOMAIN_SERVER + "/api/products";

    static CUSTOMER_API = this.DOMAIN_SERVER + "/api/customers";

    static CATEGORY_API = this.DOMAIN_SERVER + "/api/categories";

    static TABLE_API = this.DOMAIN_SERVER + "/api/tables";

    static OTP_API = this.DOMAIN_SERVER + "/api/otp";

    static ORDER_API = this.DOMAIN_SERVER + "/api/orders";

    static ORDERITEM_API = this.DOMAIN_SERVER + "/api/order-items";

    static REPORT_API = this.DOMAIN_SERVER + "/api/report";

    static PROVINCE_URL = "https://vapi.vnappmob.com/api/province/";

    static BASE_URL_CLOUD_IMAGE = "https://res.cloudinary.com/dymbrnokj/image/upload";

    static SCALE_IMAGE_W60_H50_Q100 = "c_limit,w_60,h_50,q_100";
    static SCALE_IMAGE_W60_H60_Q100 = "c_limit,w_60,h_60,q_100";
    static SCALE_IMAGE_W100_H80_Q100 = "c_limit,w_100,h_80,q_100";
    static SCALE_IMAGE_W600_H650_Q100 = "c_limit,w_600,h_650,q_100";
    static SCALE_IMAGE_W150_H100_Q100 = "c_limit,w_150,h_100,q_100";
    static SCALE_IMAGE_W250_H250_Q100 = "c_limit,w_250,h_250,q_100";

    static formatDate(input) {
        let datePart = input.match(/\d+/g),
            year = datePart[0],
            month = datePart[1], day = datePart[2];

        return day + '/' + month + '/' + year;
    }

    static formatCurrency(input) {
        return new Intl.NumberFormat('vi-VN', {style: 'currency', currency: 'VND'}).format(input)
    }

    static getCurrentYear() {
        let d = new Date();
        return d.getFullYear();
    }

    static getCurrentDay() {
        let today = new Date();
        let dd = String(today.getDate()).padStart(2, '0');
        let mm = String(today.getMonth() + 1).padStart(2, '0');
        let yyyy = today.getFullYear();
        today = yyyy + '-' + mm + '-' + dd;
        return today;
    }

    static padTo2Digits(num) {
        return num.toString().padStart(2, '0');
    }

    static setCurrentDate(date = new Date()) {
        return [
            date.getFullYear(),
            AppUtils.padTo2Digits(date.getMonth() + 1),
            AppUtils.padTo2Digits(date.getDate()),
        ].join('-');
    }

    static drawPlotChart(xArray, yArray, nameChart) {

        let data = [{
            x: xArray,
            y: yArray,
            type: "bar",
            orientation: "h",
            marker: {color: "rgba(255,0,0,0.6)"}
        }];

        let layout = {title: ""};

        Plotly.newPlot(nameChart, data, layout);
    }

    static drawBarsChart(xValues, yValues, nameChart) {
        let layout = {title: ""};

        let data = [{x: xValues, y: yValues, type: "bar"}];

        Plotly.newPlot(nameChart, data, layout);
    }

    static drawPieChart(xArray, yArray, nameChart) {

        let layout = {title: ""};

        let data = [{labels: xArray, values: yArray, hole: .4, type: "pie"}];

        Plotly.newPlot(nameChart, data, layout);
    }

    static SweetAlert = class {
        // static showDeactivateConfirmDialog() {
        //     return Swal.fire({
        //         icon: 'warning',
        //         text: 'Are you sure to deactivate the selected customer ?',
        //         showCancelButton: true,
        //         confirmButtonColor: '#3085d6',
        //         cancelButtonColor: '#d33',
        //         confirmButtonText: 'Yes, please deactivate this client !',
        //         cancelButtonText: 'Cancel',
        //     })
        // }

        static showSuccessAlert(t) {
            Swal.fire({
                icon: 'success',
                title: t,
                position: 'top-end',
                showConfirmButton: false,
                timer: 1500
            })
        }

        static showErrorAlert(t) {
            Swal.fire({
                icon: 'error',
                title: 'Warning',
                text: t,
            })
        }

        static showError401() {
            Swal.fire({
                icon: 'error',
                title: 'Truy cập bị từ chối',
                text: 'Thông tin không hợp lệ!',
            })
        }

        static showError403() {
            Swal.fire({
                icon: 'error',
                title: 'Truy cập bị từ chối',
                text: 'Bạn không được phép thực hiện chức năng này!',
            })
        }

        static showError500() {
            Swal.fire({
                icon: 'error',
                title: 'Lỗi máy chủ',
                text: 'Hệ thống Server đang có vấn đề hoặc không truy cập được.',
            })
        }

        static redirectPage(message1, message2, timer) {
            let timerInterval;
            Swal.fire({
                icon: 'success',
                title: "<br>" + message1,
                html: message2,
                timer: timer,
                timerProgressBar: true,
                didOpen: () => {
                    Swal.showLoading()
                    const b = Swal.getHtmlContainer().querySelector('b')
                    timerInterval = setInterval(() => {
                        b.textContent = Swal.getTimerLeft()
                    }, 100)
                },
                willClose: () => {
                    clearInterval(timerInterval);
                }
            }).then((result) => {
                if (result.dismiss === Swal.DismissReason.timer) {
                    console.log('I was closed by the timer')
                }
            })
        }

    }

    static IziToast = class {
        static showSuccessAlertLeft(m) {
            iziToast.success({
                title: 'OK',
                position: 'topLeft',
                timeout: 2500,
                message: m
            });
        }

        static showSuccessAlertRight(m) {
            iziToast.success({
                title: 'OK',
                position: 'topRight',
                timeout: 2500,
                message: m
            });
        }

        static showErrorAlertLeft(m) {
            iziToast.error({
                title: 'Error',
                position: 'topLeft',
                timeout: 2500,
                message: m,
            });
        }

        static showErrorAlertRight(m) {
            iziToast.error({
                title: 'Error',
                position: 'topRight',
                timeout: 2500,
                message: m
            });
        }

        static showWaringAlertLeft(m) {
            iziToast.warning({
                title: 'OK',
                position: 'topLeft',
                timeout: 2500,
                message: m
            });
        }
    }
}


class CTable {
    constructor(id, name, status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }
}

class Category {
    constructor(id, title) {
        this.id = id;
        this.title = title;
    }
}

class Staff {
    constructor(id, fullName, dob, gender, phone, locationRegion, user, staffAvatar) {
        this.id = id;
        this.fullName = fullName;
        this.dob = dob;
        this.gender = gender;
        this.phone = phone;
        this.locationRegion = locationRegion;
        this.user = user;
        this.staffAvatar = staffAvatar;
    }
}

class LocationRegion {
    constructor(id, provinceId, provinceName, districtId, districtName, wardId, wardName, address) {
        this.id = id;
        this.provinceId = provinceId;
        this.provinceName = provinceName;
        this.districtId = districtId;
        this.districtName = districtName;
        this.wardId = wardId;
        this.wardName = wardName;
        this.address = address;
    }
}

class User {
    constructor(id, username, password, role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }
}

class Role {
    constructor(id, code) {
        this.id = id;
        this.code = code;
    }
}

class StaffAvatar {
    constructor(id, fileName, fileFolder, fileUrl, fileType, cloudId, ts) {
        this.id = id;
        this.fileName = fileName;
        this.fileFolder = fileFolder;
        this.fileUrl = fileUrl;
        this.fileType = fileType;
        this.cloudId = cloudId;
        this.ts = ts;
    }
}