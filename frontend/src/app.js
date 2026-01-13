import { createApp, h } from 'vue';
import {createInertiaApp} from "@inertiajs/inertia-vue3";
import {InertiaProgress, inertiaProgress} from '@inertiajs/progress';

InertiaProgress.init()

createInertiaApp({
    resolve: name => import(`./pages/${name}.vue`),

    setup({el, App, props, plugin}) {
        createApp({ render: () => h(App, props)})
            .use(plugin)
            .mount(el)
    }
})
