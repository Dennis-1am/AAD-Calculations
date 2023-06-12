const express = require('express');
const aadRoutes = require('./routes/AAD');
const swaggerjsdoc = require('swagger-jsdoc');
const swaggerui = require('swagger-ui-express');
 
const app = express();
app.use(express.json());

const options = {
    definition: {
        openapi: "3.0.0",
        info: {
            title: "Actual Arrival Date API",
            version: "0.0.1",
            description: "Actual Arrival Date API ( Signer Jewelers - Digital IT )",
            contact: {
                name: "Dennis Lam",
                url: "https://www.linkedin.com/in/dennis-1am/",
                email: "13596105@Jewels.com"
            },
        },
        servers: [
            {
                url: "http://localhost:3000",
            },
        ],
    },
    apis: ["./routes/*.js"],
};

app.use('/', aadRoutes.router);

const spacs = swaggerjsdoc(options)
app.use(
    "/api-docs",
    swaggerui.serve,
    swaggerui.setup(spacs)
)

app.listen(3000, () => {
    console.log('Server is running on port 3000');
});