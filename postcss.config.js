module.exports = {
    plugin: [
        require("autoprefixer") ({
            overrideBrowserslist: ["last 2 versions"],
        }),
        require("cssnano")()
    ]
}