import { loadStripe } from '@stripe/stripe-js'

const STRIPE_PUBLIC_KEY = 'pk_test_51SvVp38HCfbXm3tKlj7cQF204tYrkDYYpBCt9LUau0QazoqVDwXgh2OY51VoDTeTvJMjl45ureNNIp9pHxsSTbCT00cn3G5bYe'
const API_URL = 'http://localhost:8080'

let stripePromise = null

export const getStripe = () => {
    if (!stripePromise) {
        stripePromise = loadStripe(STRIPE_PUBLIC_KEY)
    }
    return stripePromise
}

class PaymentService {

    async createPaymentIntent(orderData) {
        try {
            console.log('📤 Criando Payment Intent...', orderData)

            const response = await fetch(`${API_URL}/api/payments/create`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                credentials: 'include',
                body: JSON.stringify(orderData)
            })

            if (!response.ok) {
                const text = await response.text()
                throw new Error(text || 'Erro ao criar pagamento')
            }

            const data = await response.json()
            console.log('✅ Payment Intent criado:', data)

            return data

        } catch (error) {
            console.error('❌ Erro ao criar Payment Intent:', error)
            throw error
        }
    }

    async confirmCardPayment(clientSecret, cardElement, billingDetails) {
        try {
            console.log('Confirmando pagamento com cartão...')

            const stripe = await getStripe()

            const { error, paymentIntent } = await stripe.confirmCardPayment(
                clientSecret,
                {
                    payment_method: {
                        card: cardElement,
                        billing_details: billingDetails
                    }
                }
            )

            if (error) {
                console.error('Erro no pagamento:', error.message)
                return { success: false, error: error.message }
            }

            console.log('Pagamento confirmado!', paymentIntent)
            return { success: true, paymentIntent }

        } catch (error) {
            console.error('Erro ao confirmar pagamento:', error)
            return { success: false, error: error.message }
        }
    }
}

export default new PaymentService()