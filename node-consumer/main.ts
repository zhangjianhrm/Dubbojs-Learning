'use strict';

import {demoService} from './dubboConfig'

demoService.sayHello('kirito').then(({res,err})=>{
    console.log(res)
});


