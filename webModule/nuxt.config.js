import colors from 'vuetify/es5/util/colors'

export default {
  // Disable server-side rendering: https://go.nuxtjs.dev/ssr-mode
  ssr: false,

  // Global page headers: https://go.nuxtjs.dev/config-head
  head: {
    titleTemplate: '%s - web',
    title: 'web',
    htmlAttrs: {
      lang: 'en'
    },
    meta: [
      { charset: 'utf-8' },
      { name: 'viewport', content: 'width=device-width, initial-scale=1' },
      { hid: 'description', name: 'description', content: '' },
      { name: 'format-detection', content: 'telephone=no' }
    ],
    link: [
      { rel: 'icon', type: 'image/x-icon', href: '/favicon.ico' }
    ]
  },

  target: 'static',

  generate: {
    //dir: 'C:\\Users\\XXXXXX\\AppData\\Roaming\\Apache24\\htdocs\\',
    dir : 'C:\\Users\\39349\\IdeaProjects\\jwtSample\\servicejwt\\src\\main\\resources\\STATIC',
    cache: {
      ignore: ['renovate.json'] // ignore changes applied on this file
    },
    devtools : true
  },

  // Global CSS: https://go.nuxtjs.dev/config-css
  css: [
  ],

  // Plugins to run before rendering page: https://go.nuxtjs.dev/config-plugins
  plugins: [
  ],

  // Auto import components: https://go.nuxtjs.dev/config-components
  components: true,

  // Modules for dev and build (recommended): https://go.nuxtjs.dev/config-modules
  buildModules: [
    '@nuxt/typescript-build',
    '@nuxtjs/vuetify',
    '@nuxtjs/proxy',
    '@nuxtjs/axios',
    '@nuxtjs/auth-next'
  ],


  modules: [
  ],

  // Modules: https://go.nuxtjs.dev/config-modules
  publicRuntimeConfig: {
    axios: {
      proxy: true,
      baseURL: '',
      changeOrigin: true
    }
  },

  proxy: {
    '/api': {
      target: 'http://localhost:8080/',
      changeOrigin: true
    },
  },

  // Auth settings

  auth: {
    redirect: {
      login: '/',
      logout: '/',
      home: '/',
      callback : '/inspire'
    },
    strategies: {
      local: {
        watchLoggedIn:true,
        token: {
          property: 'access_token',
          global: true,
        },
        user: {
          property: 'user',
          autoFetch: true
        },
        endpoints: {
          login: { url: '/api/login', method: 'post' },
          logout: { url: '/api/logout', method: 'post' },
          user: { url: '/api/user', method: 'get' }
        }
      }
    }
  },

  router: {
  },

  // Vuetify module configuration: https://go.nuxtjs.dev/config-vuetify
  vuetify: {
    customVariables: ['~/assets/variables.scss'],
    theme: {
      dark: true,
      themes: {
        dark: {
          primary: colors.blue.darken2,
          accent: colors.grey.darken3,
          secondary: colors.amber.darken3,
          info: colors.teal.lighten1,
          warning: colors.amber.base,
          error: colors.deepOrange.accent4,
          success: colors.green.accent3
        }
      }
    }
  },



  // Build Configuration: https://go.nuxtjs.dev/config-build
  /**
  build: {
    publicPath: 'http://localhost:8080',
  }
   **/

}
