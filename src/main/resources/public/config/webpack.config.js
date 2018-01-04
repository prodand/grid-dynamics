const webpack = require("webpack");
const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const ExtractTextPlugin = require('extract-text-webpack-plugin');

module.exports = {

  entry: {
    'polyfills': './src/polyfills.ts',
    'vendor': './src/vendor.ts',
    'app': './src/index.ts'
  },

  resolve: {
    extensions: ['.ts', '.js']
  },

  module: {
    rules: [
      {
        test: /\.ts$/,
        loaders: [
          {
            loader: 'awesome-typescript-loader',
            options: {
              configFileName: path.resolve(__dirname, '../tsconfig.json')
            }
          }
        ]
      },
      {
        test: /\.(png|jpe?g|gif|svg|woff|woff2|ttf|eot|ico)$/,
        loader: 'file-loader?name=css/[name].[ext]'
      },
      {
        test: /\.css$/,
        use: ExtractTextPlugin.extract(
            {
              fallback: 'style-loader',
              use: 'css-loader?sourceMap',
              publicPath: "../"
            }
        )
      },
    ]
  },

  plugins: [
    new webpack.ContextReplacementPlugin(
        // The (\\|\/) piece accounts for path separators in *nix and Windows
        /angular(\\|\/)core(\\|\/)(esm(\\|\/)src|src)(\\|\/)linker/,
        path.resolve(__dirname, '../src'), // location of your src
        {} // a map of your routes
    ),
    new webpack.optimize.CommonsChunkPlugin({
      name: ['index', 'vendor', 'polyfills']
    }),
    new ExtractTextPlugin("css/[name].css"),
    new HtmlWebpackPlugin({
      template: 'src/index.html',
      inject: false,
      filename: '../index.html',
      metadata: {
        baseUrl: '/'
      }
    }),
  ],

  output: {
    path: path.resolve(__dirname, '../app'),
    filename: '[name].[chunkhash].js',
  }
};