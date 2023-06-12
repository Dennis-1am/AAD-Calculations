
const router = require('express').Router();

/**
 * @swagger
 * components:
 *   schemas:
 *       AADRequest:
 *        type: object
 *        required:
 *          - SKU
 *          - Region Time
 *        properties:
 *          SKU:
 *              type: string
 *              description: The SKU of the product to be searched in the database
 *          RegionTime:
 *              type: string
 *              description: The Region Time Zone of the User
 *       AADResponse:
 *        type: object
 *        required:
 *          - AAD
 *        properties:
 *          mm/dd/yyyy:
 *             type: string
 *             description: The Actual Arrival Date of the product
 *       APIResponse:
 *        type: object
 *        required:
 *         - message
 *         - code
 *        properties:
 *         message:
 *          type: string
 *          description: The message of the response (success or error)
 *         code:
 *          type: string
 *          description: The code of the response (200, 400, 401, 402, 403)
 */

/**
 * @swagger
 * tags:
 *  name: AAD
 *  description: Calculate the Actual Arrival Date of the product
 * paths:
 *  /AAD:
 *      post:
 *         summary: Calculate the Actual Arrival Date of the product
 *         tags: [AAD]
 *         requestBody:
 *           required: true
 *           content:
 *            application/json:
 *             schema:
    *              $ref: '#/components/schemas/AADRequest'
 *         responses:
 *               200:
 *                  description: The Actual Arrival Date of the product
 *                  content:
 *                      application/json:
 *                          schema:
 *                              $ref: '#/components/schemas/AADResponse'
 *               401:
 *                 description: The request is invalid
 *                 content:
 *                     application/json:
 *                         schema:
 *                            $ref: '#/components/schemas/APIResponse'
 *               502:
 *                 description: The request is unauthorized
 *                 content:
 *                    application/json:
 *                       schema:
 *                         $ref: '#/components/schemas/APIResponse'
 *               503:
 *                 description: The SKU is not found in the database
 *                 content:
 *                    application/json:
 *                      schema:
 *                       $ref: '#/components/schemas/APIResponse'
 *               504:
 *                 description: The Region Time is not valid
 *                 content:
 *                    application/json:
 *                      schema:
 *                       $ref: '#/components/schemas/APIResponse'
 */

const aadRoutes = (req, res, next) => {
    console.log('aadRoutes middleware executed');
    next();
}

router.post('/AAD', aadRoutes, (req, res) => {
    console.log('AAD route executed');
    const param = { SKU, RegionTime } = req.body;
    console.log(param);

    const response = {
        "mm/dd/yyyy": "01/01/2021"
    }
    res.status(200).json(response);
});

module.exports = { // Export the router and the middleware for swagger-ui/index.js
    router,
    aadRoutes
}